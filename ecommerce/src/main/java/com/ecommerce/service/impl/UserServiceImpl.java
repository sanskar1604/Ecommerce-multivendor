package com.ecommerce.service.impl;

import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;

	@Override
	public User findUserByJwtToken(String jwt) throws Exception {
		String email = jwtProvider.getEmailFromToken(jwt);
		
		User user = this.findUserByEmail(email);
	
		return user;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw new Exception("User not found with email: " + email);
		}
		
		return user;
	}

}
