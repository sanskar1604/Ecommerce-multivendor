package com.ecommerce.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.entity.HomeCategory;
import com.ecommerce.exception.HomeCategoryException;
import com.ecommerce.repository.HomeCategoryRepository;
import com.ecommerce.service.HomeCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

	private final HomeCategoryRepository homeCategoryRepository;

	@Override
	public HomeCategory createHomeCategory(HomeCategory homeCategory) {
		return homeCategoryRepository.save(homeCategory);
	}

	@Override
	public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
		if(homeCategoryRepository.findAll().isEmpty()) {
			return homeCategoryRepository.saveAll(homeCategories);
		}
		return homeCategoryRepository.findAll();
	}

	@Override
	public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws HomeCategoryException {
		HomeCategory existingCategory = homeCategoryRepository.findById(id).orElseThrow(() -> new HomeCategoryException("Category not found with id: " + id));
		
		if(homeCategory.getImage() != null) {
			existingCategory.setImage(homeCategory.getImage());
		}
		if(homeCategory.getCategoryId() != null) {
			existingCategory.setCategoryId(homeCategory.getCategoryId());
		}
		
		return homeCategoryRepository.save(existingCategory);
	}

	@Override
	public List<HomeCategory> getAllHomeCategories() {
		return homeCategoryRepository.findAll();
	}
}
