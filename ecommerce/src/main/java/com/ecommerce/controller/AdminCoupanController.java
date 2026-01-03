package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Coupan;
import com.ecommerce.entity.User;
import com.ecommerce.exception.CoupanException;
import com.ecommerce.exception.UserException;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.CartService;
import com.ecommerce.service.CoupanService;
import com.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupans")
@Tag(name = "Admin Coupan", description = "API for managind admin coupans")
public class AdminCoupanController {

	private final CoupanService coupanService;
	private final UserService userService;
	private final CartService cartService;
	
	@PostMapping("/apply")
	public ResponseEntity<Cart> applyCoupan(@RequestParam String apply,
			@RequestParam String code,
			@RequestParam double orderValue,
			@RequestHeader("Authorization") String jwt) throws UserException, CoupanException{
		User user = userService.findUserByJwtToken(jwt);
		Cart cart;
		
		if(apply.equals("true")) {
			cart = coupanService.applyCoupan(code, orderValue, user);
		}else {
			cart = coupanService.removeCoupan(code, user);
		}
		
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/admin/create")
	public ResponseEntity<Coupan> createCoupan(@RequestBody Coupan coupan){
		return ResponseEntity.ok(coupanService.createCoupan(coupan));
	}
	
	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<ApiResponse> deleteCoupan(@PathVariable Long id) throws CoupanException{
		coupanService.deleteCoupan(id);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Coupan Deleted successfully...");
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/admin/all")
	public ResponseEntity<List<Coupan>> getAllCoupan(){
		return ResponseEntity.ok(coupanService.findAllCoupans());
	}
}
