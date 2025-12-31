package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.Seller;
import com.ecommerce.exception.ProductException;
import com.ecommerce.exception.SellerException;
import com.ecommerce.request.CreateProductRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.SellerService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sellers/products")
@RequiredArgsConstructor
public class SellerProductController {

	private final ProductService productService;
	private final SellerService sellerService;
	
	@GetMapping
	public ResponseEntity<List<Product>> getProducBySellerId(@RequestHeader("Authorization") String jwt) throws SellerException{
		Seller seller = sellerService.getSellerProfile(jwt);
		
		List<Product> products = productService.getProductBySellerId(seller.getId());
		return ResponseEntity.ok(products);
	}
	
	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request, @RequestHeader("Authorization") String jwt) throws SellerException{
		Seller seller = sellerService.getSellerProfile(jwt);
		return ResponseEntity.ok(productService.createProduct(request, seller));
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException{
		productService.deleteProduct(productId);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Product deleted successfully...");
		return ResponseEntity.ok(apiResponse);
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Long productId) throws ProductException{
		return ResponseEntity.ok(productService.updateProduct(productId, product));
	}
}
