package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Wishlist;
import com.ecommerce.exception.ProductException;
import com.ecommerce.exception.UserException;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.WishlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
@Tag(name = "WishList", description = "API for managing wishlist")
public class WishlistController {

	private final WishlistService wishlistService;
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping
	@Operation(summary = "Get wishlist by userId")
	public ResponseEntity<Wishlist> getWishlistByUserId(@RequestHeader("Authorization") String jwt) throws UserException{
		User user = userService.findUserByJwtToken(jwt);
		
		return ResponseEntity.ok(wishlistService.getWishlistByUserId(user));
	}
	
	@PostMapping("/add-product/{productId}")
	@Operation(summary = "Add products to wishlist")
	public ResponseEntity<Wishlist> addProductToWishlist(@PathVariable Long productId, @RequestHeader("Authorization") String jwt) throws UserException, ProductException{
		
		Product product = productService.findProductById(productId);
		User user = userService.findUserByJwtToken(jwt);
		
		Wishlist updatedWishlist = wishlistService.addProductToWishlist(user, product);
		
		return ResponseEntity.ok(updatedWishlist);
	}
}
