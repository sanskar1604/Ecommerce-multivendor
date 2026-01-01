package com.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.domain.UserRole;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Seller;
import com.ecommerce.entity.User;
import com.ecommerce.entity.VerificationCode;
import com.ecommerce.exception.SellerException;
import com.ecommerce.exception.UserException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.SellerRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.EmailService;
import com.ecommerce.utils.OtpUtil;

import jakarta.mail.MessagingException;
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
	private final CustomUserServiceImpl customUserServiceImpl;
	private final SellerRepository sellerRepository;
	
	
	@Override
	public void sentLoginAndSignupOtp(String email, UserRole role) throws SellerException, UserException, MessagingException {
		
//		String SELLER_PREFIX = "seller_";
		String SIGNING_PREFIX = "signing_";
		
		if(email.startsWith(SIGNING_PREFIX)) {
			email = email.substring(SIGNING_PREFIX.length());
			
			if(role.equals(UserRole.ROLE_SELLER)) {
				Seller seller = sellerRepository.findByEmail(email);
				if(seller == null) {
					throw new SellerException ("Seller not exist with provided email...");
				}
			}
			else {
				User user = userRepository.findByEmail(email);
				if(user == null) {
					throw new UserException ("user not exist with provided email...");
				}
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
		String text = "Your Login/Signup otp - " + otp;
		
		emailService.sendVerificationOtpEmail(email, otp, subject, text);
		
	}

	@Override
	public String createUser(SignupRequest request) throws UserException {
		
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());
		
		if(verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())) {
			throw new UserException("wrong otp...");
		}
		
		
		
		User user = userRepository.findByEmail(request.getEmail());
		
		if(user != null) {
			throw new UserException("User already registered");
		}
		
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
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.toString()));
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return jwtProvider.generateToken(authentication);
	}

	@Override
	public AuthResponse signing(LoginRequest request) throws Exception {
		String username = request.getEmail();
		String otp = request.getOtp();
		
		Authentication authentication = authenticate(username, otp);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Login Success...");
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
		
		authResponse.setRole(UserRole.valueOf(roleName));
		return authResponse;
	}

	private Authentication authenticate(String username, String otp) throws Exception, UserException {
		UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);
		
		String SELLER_PREFIX = "seller_";
		if(username.startsWith(SELLER_PREFIX)) {
			username = username.substring(SELLER_PREFIX.length());
		}
		
		if(userDetails == null) {
			throw new BadCredentialsException("Invalid username");
		}
		
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
		
		if(verificationCode == null || !verificationCode.getOtp().equals(otp)) {
			throw new UserException("Wrong OTP");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
