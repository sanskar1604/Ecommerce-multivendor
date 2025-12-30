package com.ecommerce.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.domain.AccountStatus;
import com.ecommerce.domain.UserRole;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.Seller;
import com.ecommerce.exception.SellerException;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.SellerRepository;
import com.ecommerce.service.SellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

	private final SellerRepository sellerRepository;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final AddressRepository addressRepository;

	@Override
	public Seller getSellerProfile(String jwt) throws SellerException {
		String email = jwtProvider.getEmailFromToken(jwt);
		
		return this.getSellerByEmail(email);
	}

	@Override
	public Seller createSeller(Seller seller) throws SellerException {
		Seller existedSeller = sellerRepository.findByEmail(seller.getEmail());
		
		if(existedSeller != null) {
			throw new SellerException("Seller already exist, use different email");
		}
		Address savedAddress = addressRepository.save(seller.getPickupAddress());
		
		Seller newSeller = new Seller();
		newSeller.setEmail(seller.getEmail());
		newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
		newSeller.setSellerName(seller.getSellerName());
		newSeller.setPickupAddress(savedAddress);
		newSeller.setGSTIN(seller.getGSTIN());
		newSeller.setRole(UserRole.ROLE_SELLER);
		newSeller.setMobile(seller.getMobile());
		newSeller.setBusinessDetails(seller.getBusinessDetails());
		newSeller.setBankDetails(seller.getBankDetails());
		System.out.println("seller email: " + seller.getEmail());
		return sellerRepository.save(newSeller);
	}

	@Override
	public Seller getSellerById(Long sellerId) throws SellerException {
		Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new SellerException("Seller not found with id: " + sellerId));
		return seller;
	}

	@Override
	public Seller getSellerByEmail(String email) throws SellerException {
		Seller seller = sellerRepository.findByEmail(email);
		if(seller == null) {
			throw new SellerException("Seller is not found with email: " + email);
		}
		return seller;
	}

	@Override
	public List<Seller> getAllSellers(AccountStatus accountStatus) {
		return sellerRepository.findByAccountStatus(accountStatus);
	}

	@Override
	public Seller updateSeller(Seller seller, Long sellerId) throws SellerException {
		Seller existingSeller = this.getSellerById(sellerId);
		
		if(seller.getSellerName() != null) {
			existingSeller.setSellerName(seller.getSellerName());
		}
		if(seller.getMobile() != null) {
			existingSeller.setMobile(seller.getMobile());
		}
		if(seller.getEmail() != null) {
			existingSeller.setMobile(seller.getMobile());
		}
		if(seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
			existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());;
		}
		if(seller.getBankDetails() != null && seller.getBankDetails().getAccountHolderName() != null 
				&& seller.getBankDetails().getIfscCode() != null 
				&& seller.getBankDetails().getAccountNumber() != null) {
			existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
			existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
			existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
		}
		if(seller.getPickupAddress() != null 
				&& seller.getPickupAddress().getAddress() != null
				&& seller.getPickupAddress().getMobile() != null
				&& seller.getPickupAddress().getCity() != null
				&& seller.getPickupAddress().getState() != null
				&& seller.getPickupAddress().getPincode() != null) {
			existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
			existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
			existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
			existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
			existingSeller.getPickupAddress().setPincode(seller.getPickupAddress().getPincode());
		}
		if(seller.getGSTIN() != null) {
			existingSeller.setGSTIN(seller.getGSTIN());
		}
		
		return sellerRepository.save(existingSeller);
	}

	@Override
	public void deleteSeller(Long sellerId) throws SellerException {
		Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new SellerException("Seller not found with id: " + sellerId));
		sellerRepository.delete(seller);
		
	}

	@Override
	public Seller verifyEmail(String email, String otp) throws SellerException {
		Seller seller = getSellerByEmail(email);
		seller.setIsEmailVerified(true);
		return sellerRepository.save(seller);
	}

	@Override
	public Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws SellerException {
		Seller seller = getSellerById(sellerId);
		seller.setAccountStatus(accountStatus);
		return sellerRepository.save(seller);
	}
}
