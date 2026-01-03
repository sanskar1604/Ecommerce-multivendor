package com.ecommerce.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.Review;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ReviewException;
import com.ecommerce.repository.ReviewRepository;
import com.ecommerce.request.CreateReviewRequest;
import com.ecommerce.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewRepository reviewRepository;

	@Override
	public Review createReview(CreateReviewRequest request, User user, Product product) {
		
		Review review = new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReviewText(request.getReviewText());
		review.setRating(request.getReviewRating());
		review.setProductImages(request.getProductImages());
		
		product.getReviews().add(review);
		
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getReviewByProductId(Long productId) {
		return reviewRepository.findByProductId(productId);
	}

	@Override
	public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws ReviewException {
		Review review = getReviewById(reviewId);
		
		if(review.getUser().getId().equals(userId)) {
			review.setReviewText(reviewText);
			review.setRating(rating);
			return reviewRepository.save(review);
		}
		throw new ReviewException("You can't update this review");
	}

	@Override
	public void deleteReview(Long reviewId, Long userId) throws ReviewException {
		Review review = getReviewById(reviewId);
		if(!review.getUser().getId().equals(userId)) {
			throw new ReviewException("You can't delete this review");
		}
		reviewRepository.delete(review);
	}

	@Override
	public Review getReviewById(Long reviewId) throws ReviewException {
		return reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException("Review not found with id: " + reviewId));
	}

}
