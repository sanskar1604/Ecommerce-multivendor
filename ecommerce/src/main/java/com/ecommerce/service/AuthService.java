package com.ecommerce.service;

import com.ecommerce.response.SignupRequest;

public interface AuthService {

	String createUser(SignupRequest request);
}
