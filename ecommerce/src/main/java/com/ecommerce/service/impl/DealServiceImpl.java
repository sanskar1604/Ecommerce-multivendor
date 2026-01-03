package com.ecommerce.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.entity.Deal;
import com.ecommerce.entity.HomeCategory;
import com.ecommerce.exception.DealException;
import com.ecommerce.exception.HomeCategoryException;
import com.ecommerce.repository.DealRepository;
import com.ecommerce.repository.HomeCategoryRepository;
import com.ecommerce.service.DealService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
	
	private final DealRepository dealRepository;
	private final HomeCategoryRepository homeCategoryRepository;

	@Override
	public List<Deal> getDeals() {
		return dealRepository.findAll();
	}

	@Override
	public Deal createDeal(Deal deal) {
		HomeCategory homeCategory = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
		
		Deal newDeal = new Deal();
		newDeal.setCategory(homeCategory);
		newDeal.setDiscount(deal.getDiscount());
		
		return dealRepository.save(newDeal);
	}

	@Override
	public Deal updateDeal(Long dealId, Deal deal) throws DealException, HomeCategoryException {
		
		Deal existingDeal = dealRepository.findById(dealId).orElseThrow(() -> new DealException("Deal not found with id: " + dealId));
		
		HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId())
				.orElseThrow(() -> new HomeCategoryException("Home Category not found with id: " + deal.getCategory().getId()));
		
		if(existingDeal != null) {
			if(deal.getDiscount() != null) {
				existingDeal.setDiscount(deal.getDiscount());
			}
			if(category != null) {
				existingDeal.setCategory(category);
			}
			return dealRepository.save(existingDeal);
		}
		
		throw new DealException("Deal not found...");
	}

	@Override
	public void deleteDeal(Long dealId) throws DealException {
		Deal deal = dealRepository.findById(dealId).orElseThrow(() -> new DealException("Deal not found with id: " + dealId));
		
		dealRepository.delete(deal);
	}

}
