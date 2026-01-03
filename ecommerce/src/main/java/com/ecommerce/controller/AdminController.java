package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.AccountStatus;
import com.ecommerce.entity.Seller;
import com.ecommerce.exception.SellerException;
import com.ecommerce.service.HomeCategoryService;
import com.ecommerce.service.HomeService;
import com.ecommerce.service.SellerService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Admin", description = "API for managing admin")
public class AdminController {

	private final SellerService sellerService;
	
	public ResponseEntity<Seller> updateSellerStatus(@PathVariable Long id, @PathVariable AccountStatus status) throws SellerException{
		
		return ResponseEntity.ok(sellerService.updateSellerAccountStatus(id, status));
	}
}
