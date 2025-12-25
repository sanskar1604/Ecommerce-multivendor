package com.ecommerce.service;

import com.ecommerce.response.SignupRequest;

public interface AuthService {

	void sentLoginAndSignupOtp(String email) throws Exception;
	String createUser(SignupRequest request) throws Exception;
}
