package com.ecommerce.service;

import java.util.Set;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.PaymentOrder;
import com.ecommerce.entity.User;
import com.ecommerce.exception.PaymentOrderException;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {

	PaymentOrder createOrder(User user, Set<Order> orders);
	
	PaymentOrder getPaymentOrderById(Long orderId) throws PaymentOrderException;
	
	PaymentOrder getPaymentOrderByPaymentId(String orderId) throws PaymentOrderException;
	
	Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;
	
	PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException;
	
	String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
