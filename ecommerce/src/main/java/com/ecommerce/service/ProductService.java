package com.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.Seller;
import com.ecommerce.exception.ProductException;
import com.ecommerce.request.CreateProductRequest;

public interface ProductService {

	Product createProduct(CreateProductRequest request, Seller seller);
	
	void deleteProduct(Long productId) throws ProductException;
	
	Product updateProduct(Long productId, Product product) throws ProductException;
	
	Product findProductById(Long productId) throws ProductException;
	
	List<Product> searchProducts(String query);
	
	public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, 
			Integer minDiscount, String sort, String stock, Integer pageNumber);
	
	List<Product> getProductBySellerId(Long sellerId);
	
}
