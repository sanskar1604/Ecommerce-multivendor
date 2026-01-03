package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.Home;
import com.ecommerce.entity.HomeCategory;

public interface HomeService {

	Home createHomePageData(List<HomeCategory> allCategories);
}
