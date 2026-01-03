package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Coupan;
import com.ecommerce.entity.User;
import com.ecommerce.exception.CoupanException;

public interface CoupanService {

	Cart applyCoupan(String code, double orderValue, User user) throws CoupanException;
	
	Cart removeCoupan(String code, User user) throws CoupanException;
	
	Coupan findCoupanById(Long coupanId) throws CoupanException;
	
	Coupan createCoupan(Coupan coupan);
	
	List<Coupan> findAllCoupans();
	
	void deleteCoupan(Long coupanId) throws CoupanException;
}
