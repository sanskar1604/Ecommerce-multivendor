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
import com.ecommerce.entity.VerificationCode;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.response.SignupRequest;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.EmailService;
import com.ecommerce.utils.OtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	private final JwtProvider jwtProvider;
	private final VerificationCodeRepository verificationCodeRepository;
	private final EmailService emailService; 
	
	
	@Override
	public void sentLoginAndSignupOtp(String email) throws Exception {
		
		String SIGNING_PREFIX = "signing_";
		
		if(email.startsWith(SIGNING_PREFIX)) {
			email = email.substring(SIGNING_PREFIX.length());
			
			User user = userRepository.findByEmail(email);
			if(user == null) {
				throw new Exception ("user not exist with provided email...");
			}
		}
		
		VerificationCode isExist = verificationCodeRepository.findByEmail(email);
		
		if(isExist != null) {
			verificationCodeRepository.delete(isExist);
		}
		
		String otp = OtpUtil.generateOtp();
		
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(otp);
		verificationCode.setEmail(email);
		
		verificationCodeRepository.save(verificationCode);
		
		String subject = "Ecommerce Login/Signup Otp";
		String text = "Your Login/Signup otp - ";
		
		emailService.sendVerificationOtpEmail(email, otp, subject, text);
		
	}

	@Override
	public String createUser(SignupRequest request) throws Exception {
		
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());
		
		if(verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())) {
			throw new Exception("wrong otp...");
		}
		
		
		
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
