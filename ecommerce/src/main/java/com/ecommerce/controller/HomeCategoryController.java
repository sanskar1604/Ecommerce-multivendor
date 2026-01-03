package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Home;
import com.ecommerce.entity.HomeCategory;
import com.ecommerce.exception.HomeCategoryException;
import com.ecommerce.service.HomeCategoryService;
import com.ecommerce.service.HomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Home Category", description = "API for managing home category")
public class HomeCategoryController {

	private final HomeCategoryService homeCategoryService;
	private final HomeService homeService;
	
	
	@PostMapping("/home/categories")
	@Operation(summary = "Create Home Category")
	public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories){
		List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
		Home home = homeService.createHomePageData(categories);
		
		return ResponseEntity.ok(home);
	}
	
	@GetMapping("/admin/home-category")
	@Operation(summary = "Get Home Category")
	public ResponseEntity<List<HomeCategory>> getHomeCategory(){
		return ResponseEntity.ok(homeCategoryService.getAllHomeCategories());
	}
	
	@PutMapping("/admin/home-category/{id}")
	@Operation(summary = "Update home category by id")
	public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable Long id, @RequestBody HomeCategory homeCategory) throws HomeCategoryException{
		return ResponseEntity.ok(homeCategoryService.updateHomeCategory(homeCategory, id));
	}
}
