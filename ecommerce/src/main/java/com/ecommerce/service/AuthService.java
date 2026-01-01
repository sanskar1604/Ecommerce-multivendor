package com.ecommerce.service;

import com.ecommerce.domain.UserRole;
import com.ecommerce.exception.SellerException;
import com.ecommerce.exception.UserException;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;

import jakarta.mail.MessagingException;

public interface AuthService {

	void sentLoginAndSignupOtp(String email, UserRole role) throws SellerException, UserException, MessagingException;
	String createUser(SignupRequest request) throws UserException;
	AuthResponse signing(LoginRequest request) throws UserException, Exception;
}
