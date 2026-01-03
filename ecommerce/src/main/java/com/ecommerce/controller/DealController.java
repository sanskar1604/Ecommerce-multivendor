package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Deal;
import com.ecommerce.exception.DealException;
import com.ecommerce.exception.HomeCategoryException;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.DealService;
import com.ecommerce.service.SellerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
@Tag(name = "Deal", description = "API for managing deals")
public class DealController {

	private final DealService dealService;
	
	@PostMapping
	@Operation(summary = "Create Deal")
	public ResponseEntity<Deal> createDeal(@RequestBody Deal deal){
		return ResponseEntity.ok(dealService.createDeal(deal));
	}
	
	@PutMapping("/{dealId}")
	@Operation(summary = "Update deal by dealId")
	public ResponseEntity<Deal> updateDeal(@PathVariable Long dealId, @RequestBody Deal deal) throws DealException, HomeCategoryException{
		return ResponseEntity.ok(dealService.updateDeal(dealId, deal));
	}
	
	@DeleteMapping("/{dealId}")
	@Operation(summary = "Delete deal by dealId")
	public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long dealId) throws DealException {
		dealService.deleteDeal(dealId);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Deal deleted successfully");
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping
	@Operation(summary = "Get all deals")
	public ResponseEntity<List<Deal>> getAllDeal() {
		return ResponseEntity.ok(dealService.getDeals());
	}
}
