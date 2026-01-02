package com.ecommerce.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Product;
import com.ecommerce.exception.ProductException;
import com.ecommerce.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "API for managing products")
public class ProductController {

	private final ProductService productService;
	
	@GetMapping("/{productId}")
	@Operation(summary = "Get product by productId")
	public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException{
		return ResponseEntity.ok(productService.findProductById(productId));
	}
	
	@GetMapping("/search")
	@Operation(summary = "Search product")
	public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false) String query){
		return ResponseEntity.ok(productService.searchProducts(query));
	}
	
	@GetMapping
	@Operation(summary = "Get all products")
	public ResponseEntity<Page<Product>> getAllProduct(@RequestParam(required = false) String category,
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) String color,
			@RequestParam(required = false) String size,
			@RequestParam(required = false) Integer minPrice,
			@RequestParam(required = false) Integer maxPrice,
			@RequestParam(required = false) Integer minDiscount,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String stock,
			@RequestParam(defaultValue = "0") Integer pageNumber){
		return ResponseEntity.ok(productService.getAllProducts(category, brand, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber));
	}
}
