package com.ecommerce.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequest {

	private String reviewText;
	private double reviewRating;
	private List<String> productImages;
}
