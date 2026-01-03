package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.Deal;
import com.ecommerce.exception.DealException;
import com.ecommerce.exception.HomeCategoryException;

public interface DealService {

	List<Deal> getDeals();
	
	Deal createDeal(Deal deal);
	
	Deal updateDeal(Long dealId, Deal deal) throws DealException, HomeCategoryException;
	
	void deleteDeal(Long dealId) throws DealException;
}
