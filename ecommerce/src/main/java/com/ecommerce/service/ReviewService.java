package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.Review;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ReviewException;
import com.ecommerce.request.CreateReviewRequest;

public interface ReviewService {

	Review createReview(CreateReviewRequest request, User user, Product product);
	
	List<Review> getReviewByProductId(Long productId);
	
	Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws ReviewException;
	
	void deleteReview(Long reviewId, Long userId) throws ReviewException;
	
	Review getReviewById(Long reviewId) throws ReviewException;
}
