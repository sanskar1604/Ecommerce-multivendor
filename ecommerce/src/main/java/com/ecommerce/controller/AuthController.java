package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.UserRole;
import com.ecommerce.entity.User;
import com.ecommerce.entity.VerificationCode;
import com.ecommerce.request.LoginOtpRequest;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;
import com.ecommerce.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "API for authentication and login/signup")
public class AuthController {
	
	private final AuthService authService;

	@PostMapping("/signup")
	@Operation(summary = "Create user")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request) throws Exception{
		
		String jwt = authService.createUser(request);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Register Success...");
		authResponse.setRole(UserRole.ROLE_CUSTOMER);
		
		return ResponseEntity.ok(authResponse);
	}
	
	@PostMapping("/send/login-signup-otp")
	@Operation(summary = "Sent OTP handler")
	public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest request) throws Exception{
		
		authService.sentLoginAndSignupOtp(request.getEmail(), request.getRole());
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Otp sent successfully...");
		
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping("/signing")
	@Operation(summary = "Login Handler")
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) throws Exception{
		
		AuthResponse authResponse = authService.signing(request);
		
		return ResponseEntity.ok(authResponse);
	}
}
