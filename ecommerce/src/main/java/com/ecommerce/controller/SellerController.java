package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.domain.AccountStatus;
import com.ecommerce.entity.Seller;
import com.ecommerce.entity.SellerReport;
import com.ecommerce.entity.VerificationCode;
import com.ecommerce.exception.SellerException;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.EmailService;
import com.ecommerce.service.SellerService;
import com.ecommerce.utils.OtpUtil;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
@Tag(name = "Seller", description = "API for managing seller")
public class SellerController {
	
	private final VerificationCodeRepository verificationCodeRepository;
	private final AuthService authService;
	private final SellerService sellerService;
	private final EmailService emailService;
	private final JwtProvider jwtProvider;
	

	@PostMapping("/login")
	@Operation(summary = "Login Seller")
	public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception{
		String otp = req.getOtp();
		String email = req.getEmail();
		
//		VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
//		if(verificationCode == null && verificationCode.getOtp().equals(req.getOtp())) {
//			throw new Exception("wrong otp...");
//		}
		
		req.setEmail("seller_"+email);
		System.out.println("seller login email: " + email);
		AuthResponse authResponse = authService.signing(req);
		
		return ResponseEntity.ok(authResponse);
	}
	
	@PatchMapping("/verify/{sellerOtp}")
	@Operation(summary = "Verify Seller Email")
	public ResponseEntity<Seller> verifySellerEmail(@PathVariable String sellerOtp) throws Exception{
		
		VerificationCode verificationCode = verificationCodeRepository.findByOtp(sellerOtp);
		
		if(verificationCode == null || !verificationCode.getOtp().equals(sellerOtp)) {
			throw new Exception("wrong otp...");
		}
		Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), sellerOtp);
		
		return ResponseEntity.ok(seller);
	}
	
	@PostMapping
	@Operation(summary = "Create Seller")
	public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws SellerException, MessagingException{
		System.out.println("Seller email in controller" + seller.getSellerName());
		Seller savedSeller = sellerService.createSeller(seller);
		
		String otp = OtpUtil.generateOtp();
		
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(otp);
		verificationCode.setEmail(seller.getEmail());
		verificationCodeRepository.save(verificationCode);
		
		
		String subject = "Ecommerce multivendor Email Verification code";
		String text = "Welcome to Ecommerce-multivendor, verify your account using this link";
		String frontendUrl = "http://localhost:3000/verify-seller/";
		
		emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text+frontendUrl);
		
		
		return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get Seller by sellerId")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException{
		return ResponseEntity.ok(sellerService.getSellerById(id));
	}
	
	@GetMapping("/profile")
	@Operation(summary = "Get Seller profile")
	public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception{
		Seller seller = sellerService.getSellerProfile(jwt);
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}
	
//	@GetMapping("/report")
//	public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt) throws SellerException{
//		String email = jwtProvider.getEmailFromToken(jwt);
//		Seller seller = sellerService.getSellerByEmail(email);
//		SellerReport report = sellerReportService.getSellerReport(seller);
//		return new ResponseEntity<>(report, HttpStatus.OK);
//	}
	
	@GetMapping
	@Operation(summary = "Get all Sellers")
	public ResponseEntity<List<Seller>> getAllSeller(@RequestParam(required=false) AccountStatus accountStatus) throws SellerException{
		List<Seller> sellers = sellerService.getAllSellers(accountStatus);
		return new ResponseEntity<>(sellers, HttpStatus.OK);
	}
	
	@PutMapping
	@Operation(summary = "Update Seller")
	public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws SellerException{
		Seller profile = sellerService.getSellerProfile(jwt);
		Seller updatedSeller = sellerService.updateSeller(seller, profile.getId());
		return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete Seller by sellerId")
	public ResponseEntity<ApiResponse> deleteSeller(@PathVariable Long id) throws SellerException{
		sellerService.deleteSeller(id);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Seller deleted successfully...");
		return ResponseEntity.ok(apiResponse);
	}
}
