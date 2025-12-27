package com.ecommerce.service;

import java.util.List;

import com.ecommerce.domain.AccountStatus;
import com.ecommerce.entity.Seller;

public interface SellerService {

	Seller getSellerProfile(String jwt) throws Exception;
	
	Seller createSeller(Seller seller) throws Exception;
	
	Seller getSellerById(Long sellerId) throws Exception;
	
	Seller getSellerByEmail(String email) throws Exception;
	
	List<Seller> getAllSellers(AccountStatus accountStatus);
	
	Seller updateSeller(Seller seller, Long sellerId) throws Exception;
	
	void deleteSeller(Long sellerId) throws Exception;
	
	Seller verifyEmail(String email, String otp) throws Exception;
	
	Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws Exception;
}
