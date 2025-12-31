package com.ecommerce.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(SellerException.class)
	public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException ex, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setError(ex.getMessage());
		errorDetails.setDetails(request.getDescription(false));
		errorDetails.setTimeStamp(LocalDateTime.now());
		
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ProductException.class)
	public ResponseEntity<ErrorDetails> productExceptionHandler(ProductException ex, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setError(ex.getMessage());
		errorDetails.setDetails(request.getDescription(false));
		errorDetails.setTimeStamp(LocalDateTime.now());
		
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CartItemException.class)
	public ResponseEntity<ErrorDetails> cartItemExceptionHandler(CartItemException ex, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setError(ex.getMessage());
		errorDetails.setDetails(request.getDescription(false));
		errorDetails.setTimeStamp(LocalDateTime.now());
		
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}
