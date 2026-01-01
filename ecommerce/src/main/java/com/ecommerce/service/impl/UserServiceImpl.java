package com.ecommerce.service.impl;

import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.entity.User;
import com.ecommerce.exception.UserException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;

	@Override
	public User findUserByJwtToken(String jwt) throws UserException {
		String email = jwtProvider.getEmailFromToken(jwt);
		
		User user = this.findUserByEmail(email);
	
		return user;
	}

	@Override
	public User findUserByEmail(String email) throws UserException {
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw new UserException("User not found with email: " + email);
		}
		
		return user;
	}

}
