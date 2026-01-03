package com.ecommerce.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Coupan;
import com.ecommerce.entity.User;
import com.ecommerce.exception.CoupanException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.CoupanRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CoupanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoupanServiceImpl implements CoupanService {

	private final CoupanRepository coupanRepository;
	private final CartRepository cartRepository;
	private final UserRepository userRepository;

	@Override
	public Cart applyCoupan(String code, double orderValue, User user) throws CoupanException {
		
		Coupan coupan = coupanRepository.findByCode(code);
		Cart cart = cartRepository.findByUserId(user.getId());
		
		if(coupan == null) {
			throw new CoupanException("Coupan not valid...");
		}
		if(user.getUsedCoupans().contains(coupan)) {
			throw new CoupanException("Coupan already used...");
		}
		if(orderValue <= coupan.getMinimumOrderValue()) {
			throw new CoupanException("Valid for minimum order value: " + coupan.getMinimumOrderValue());
		}
		if(coupan.isActive() && 
				LocalDate.now().isAfter(coupan.getValidityStartDate()) &&
				LocalDate.now().isBefore(coupan.getValidityEndDate())){
			user.getUsedCoupans().add(coupan);
			userRepository.save(user);
			
			double discountedPrice = (cart.getTotalSellingPrice()*coupan.getDiscountPercentage())/100;
			
			cart.setTotalSellingPrice(cart.getTotalSellingPrice()-discountedPrice);
			cart.setCoupanCode(code);
			cartRepository.save(cart);
			return cart;
		}
		throw new CoupanException("Coupan not valid...");
	}

	@Override
	public Cart removeCoupan(String code, User user) throws CoupanException {
		Coupan coupan = coupanRepository.findByCode(code);
		if(coupan == null) {
			throw new CoupanException("Coupan not found...");
		}
		
		Cart cart = cartRepository.findByUserId(user.getId());
		
		double discountedPrice = (cart.getTotalSellingPrice()*coupan.getDiscountPercentage())/100;
		cart.setTotalSellingPrice(cart.getTotalSellingPrice()+discountedPrice);
		cart.setCoupanCode(null);
		
		return cartRepository.save(cart);
	}

	@Override
	public Coupan findCoupanById(Long coupanId) throws CoupanException {
		return coupanRepository.findById(coupanId).orElseThrow(() -> new CoupanException("Coupan not found with id: " + coupanId));
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public Coupan createCoupan(Coupan coupan) {
		return coupanRepository.save(coupan);
	}

	@Override
	public List<Coupan> findAllCoupans() {
		return coupanRepository.findAll();
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public void deleteCoupan(Long coupanId) throws CoupanException {
		findCoupanById(coupanId);
		coupanRepository.deleteById(coupanId);
	}
}
