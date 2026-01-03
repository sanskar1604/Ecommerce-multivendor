package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.Review;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ProductException;
import com.ecommerce.exception.ReviewException;
import com.ecommerce.exception.UserException;
import com.ecommerce.request.CreateReviewRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ReviewService;
import com.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Review", description = "API for managing review")
public class ReviewController {

	private final ReviewService reviewService;
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping("/products/{productId}/reviews")
	@Operation(summary = "Get reviews by productId")
	public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId){
		return ResponseEntity.ok(reviewService.getReviewByProductId(productId));
	}
	
	@PostMapping("/products/{productId}/reviews")
	@Operation(summary = "Write review")
	public ResponseEntity<Review> writeReview(@RequestBody CreateReviewRequest request, 
			@PathVariable Long productId,
			@RequestHeader("Authorization") String jwt) throws UserException, ProductException{
		User user = userService.findUserByJwtToken(jwt);
		Product product = productService.findProductById(productId);
		
		return ResponseEntity.ok(reviewService.createReview(request, user, product));
	}
	
	@PatchMapping("/reviews/{reviewId}")
	@Operation(summary = "Update review")
	public ResponseEntity<Review> updateReview(@RequestBody CreateReviewRequest request, 
			@PathVariable Long reviewId,
			@RequestHeader("Authorization") String jwt) throws UserException, ReviewException {
		
		User user = userService.findUserByJwtToken(jwt);
		
		return ResponseEntity.ok(reviewService.updateReview(reviewId, request.getReviewText(), request.getReviewRating(), user.getId()));
	}
	
	@DeleteMapping("/reviews/{reviewId}")
	@Operation(summary = "Delete review")
	public ResponseEntity<ApiResponse> deleteReview( 
			@PathVariable Long reviewId,
			@RequestHeader("Authorization") String jwt) throws UserException, ReviewException {
		
		User user = userService.findUserByJwtToken(jwt);
		reviewService.deleteReview(reviewId, user.getId());
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Review deleted successfully...");
		return ResponseEntity.ok(apiResponse);
	}
}
