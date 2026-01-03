package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.Seller;
import com.ecommerce.entity.Transaction;

public interface TransactionService {

	Transaction createTransaction(Order order);
	
	List<Transaction> getTransactionBySellerId(Seller seller);
	
	List<Transaction> getAllTransactions();
}
