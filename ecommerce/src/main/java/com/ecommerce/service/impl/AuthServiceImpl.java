package com.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.domain.UserRole;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.response.SignupRequest;
import com.ecommerce.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	private final JwtProvider jwtProvider;

	@Override
	public String createUser(SignupRequest request) {
		
		User user = userRepository.findByEmail(request.getEmail());
		
		if(user != null) {
			User createdUser = new User();
			createdUser.setEmail(request.getEmail());
			createdUser.setFullName(request.getFullName());
			createdUser.setRole(UserRole.ROLE_CUSTOMER);
			createdUser.setMobile("5585445662");
			createdUser.setPassword(passwordEncoder.encode(request.getOtp()));
			
			user = userRepository.save(createdUser);
			
			Cart cart = new Cart();
			cart.setUser(user);
			cartRepository.save(cart);
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.toString()));
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return jwtProvider.generateToken(authentication);
	}

}
