package com.ecommerce.service;

import java.util.List;

import com.ecommerce.domain.AccountStatus;
import com.ecommerce.entity.Seller;
import com.ecommerce.exception.SellerException;

public interface SellerService {

	Seller getSellerProfile(String jwt) throws SellerException;
	
	Seller createSeller(Seller seller) throws SellerException;
	
	Seller getSellerById(Long sellerId) throws SellerException;
	
	Seller getSellerByEmail(String email) throws SellerException;
	
	List<Seller> getAllSellers(AccountStatus accountStatus);
	
	Seller updateSeller(Seller seller, Long sellerId) throws SellerException;
	
	void deleteSeller(Long sellerId) throws SellerException;
	
	Seller verifyEmail(String email, String otp) throws SellerException;
	
	Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws SellerException;
}
