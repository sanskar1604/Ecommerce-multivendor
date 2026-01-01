package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.request.AddItemRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.CartItemService;
import com.ecommerce.service.CartService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "API for managing cart")
public class CartController {

	private final CartItemService cartItemService;
	private final CartService cartService;
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping
	@Operation(summary = "Find user cart handler")
	public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		
		Cart cart = cartService.findUserCart(user);
		
		return ResponseEntity.ok(cart);
	}
	
	@PutMapping("/add")
	@Operation(summary = "Add item to cart")
	public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest request, @RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		
		Product product = productService.findProductById(request.getProductId());
		
		CartItem item = cartService.addCartItem(user, product, request.getSize(), request.getQuantity());
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Item added to cart successfully...");
		
		return ResponseEntity.ok(item);
	}
	
	@DeleteMapping("/{cartItemId}")
	@Operation(summary = "Delete cart item using cartItemId")
	public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		cartItemService.removeCartItem(user.getId(), cartItemId);
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Cart item deleted successfully...");
		return ResponseEntity.ok(apiResponse);
	}
	
	@PutMapping("/item/{cartItemId}")
	@Operation(summary = "Add item to cart")
	public ResponseEntity<CartItem> updateCartItemHandler(@PathVariable Long cartItemId, @RequestBody CartItem cartItem,
			@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		
		CartItem updatedCartItem = null;
		
		if(cartItem.getQuantity() > 0) {
			updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
		}
		
		return ResponseEntity.ok(updatedCartItem);
	}
}
