package com.ecommerce.service;

import com.ecommerce.domain.UserRole;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;

public interface AuthService {

	void sentLoginAndSignupOtp(String email, UserRole role) throws Exception;
	String createUser(SignupRequest request) throws Exception;
	AuthResponse signing(LoginRequest request);
}
