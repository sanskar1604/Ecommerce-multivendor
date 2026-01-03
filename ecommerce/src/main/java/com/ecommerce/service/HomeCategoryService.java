package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.HomeCategory;
import com.ecommerce.exception.HomeCategoryException;

public interface HomeCategoryService {

	HomeCategory createHomeCategory(HomeCategory homeCategory);
	
	List<HomeCategory> createCategories(List<HomeCategory> homeCategories);
	
	HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws HomeCategoryException;
	
	List<HomeCategory> getAllHomeCategories();
}
