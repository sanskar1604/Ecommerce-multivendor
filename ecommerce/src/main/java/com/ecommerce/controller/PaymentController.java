package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.PaymentOrder;
import com.ecommerce.entity.Seller;
import com.ecommerce.entity.SellerReport;
import com.ecommerce.entity.User;
import com.ecommerce.exception.PaymentOrderException;
import com.ecommerce.exception.SellerException;
import com.ecommerce.exception.UserException;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.PaymentLinkResponse;
import com.ecommerce.service.PaymentService;
import com.ecommerce.service.SellerReportService;
import com.ecommerce.service.SellerService;
import com.ecommerce.service.UserService;
import com.razorpay.RazorpayException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(name = "Payment", description = "API for managing payment")
public class PaymentController {
	
	private final PaymentService paymentService;
	private final UserService userService;
	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	
	public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable String paymentId,
			@RequestParam String paymentLinkId,
			@RequestHeader("Authorization") String jwt) throws PaymentOrderException, RazorpayException, SellerException, UserException{
		User user = userService.findUserByJwtToken(jwt);
		
		PaymentLinkResponse paymentResponse;
		
		PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
		
		boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);
		
		if(paymentSuccess) {
			for(Order order: paymentOrder.getOrders()) {
//				transactionService.createTransaction(order);
				Seller seller = sellerService.getSellerById(order.getSellerId());
				SellerReport report = sellerReportService.getSellerReport(seller);
				report.setTotalOrders(report.getTotalOrders()+1);
				report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
				report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
				sellerReportService.updateSellerReport(report);
			}
		}
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Payment successfull...");
		
		return ResponseEntity.ok(apiResponse);
	}

}
