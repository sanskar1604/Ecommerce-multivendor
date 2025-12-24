package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.UserRole;
import com.ecommerce.entity.User;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;
import com.ecommerce.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request){
		
		String jwt = authService.createUser(request);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Register Success...");
		authResponse.setRole(UserRole.ROLE_CUSTOMER);
		
		return ResponseEntity.ok(authResponse);
		
	}
}
