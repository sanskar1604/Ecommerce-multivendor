package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Seller;
import com.ecommerce.entity.Transaction;
import com.ecommerce.exception.SellerException;
import com.ecommerce.service.SellerService;
import com.ecommerce.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "API for managing transaction")
public class TransactionController {
	
	private final TransactionService transactionService;
	private final SellerService sellerService;

	@GetMapping("/seller")
	@Operation(summary = "Get Transactions by seller")
	public ResponseEntity<List<Transaction>> getTransactionBySeller(@RequestHeader("Authorization") String jwt) throws SellerException{
		Seller seller = sellerService.getSellerProfile(jwt);
		
		List<Transaction> transactions = transactionService.getTransactionBySellerId(seller);
		
		return ResponseEntity.ok(transactions);
	}
	
	@GetMapping
	@Operation(summary = "Get all transactions")
	public ResponseEntity<List<Transaction>> getAllTransactions() {
		return ResponseEntity.ok(transactionService.getAllTransactions());
	}
}
