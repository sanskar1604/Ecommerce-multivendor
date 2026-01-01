package com.ecommerce.service;

import com.ecommerce.entity.User;
import com.ecommerce.exception.UserException;

public interface UserService {

	User findUserByJwtToken(String jwt) throws UserException;
	
	User findUserByEmail(String email) throws UserException;
}
