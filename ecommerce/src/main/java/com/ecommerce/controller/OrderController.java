package com.ecommerce.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.PaymentMethod;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.PaymentOrder;
import com.ecommerce.entity.Seller;
import com.ecommerce.entity.SellerReport;
import com.ecommerce.entity.User;
import com.ecommerce.exception.OrderException;
import com.ecommerce.exception.SellerException;
import com.ecommerce.exception.UserException;
import com.ecommerce.repository.PaymentOrderRepository;
import com.ecommerce.response.PaymentLinkResponse;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.PaymentService;
import com.ecommerce.service.SellerReportService;
import com.ecommerce.service.SellerService;
import com.ecommerce.service.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "API for managing orders")
public class OrderController {

	private final OrderService orderService;
	private final UserService userService;
	private final CartService cartService;
	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	private final PaymentService paymentService;
	private final PaymentOrderRepository paymentOrderRepository;
	
	@PostMapping
	@Operation(summary = "Create Order")
	public ResponseEntity<PaymentLinkResponse> createOrderHandler(@RequestBody Address shippingAddresss,
			@RequestParam PaymentMethod paymentMethod,
			@RequestHeader("Authorization") String jwt) throws UserException, RazorpayException, StripeException{
		User user = userService.findUserByJwtToken(jwt);
		
		Cart cart = cartService.findUserCart(user);
		
		Set<Order> orders = orderService.createOrder(user, shippingAddresss, cart);
		
		PaymentOrder paymentOrder = paymentService.createOrder(user, orders);
		
		PaymentLinkResponse response = new PaymentLinkResponse();
		
		if(paymentMethod.equals(PaymentMethod.RAZORPAY)) {
			PaymentLink payment = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
			String paymentUrl = payment.get("short_url");
			String paymentUrlId = payment.get("id");
			
			response.setPayment_link_id(paymentUrl);
			
			paymentOrder.setPaymentLinkId(paymentUrlId);
			paymentOrderRepository.save(paymentOrder);
		}else {
			String paymentUrl = paymentService.createStripePaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
			
			response.setPayment_link_id(paymentUrl);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/user")
	@Operation(summary = "User order history")
	public ResponseEntity<List<Order>> userOrderHistoryHandler(@RequestHeader("Authorization") String jwt) throws UserException{
		User user = userService.findUserByJwtToken(jwt);
		
		return ResponseEntity.ok(orderService.userOrderHistory(user.getId()));
	}
	
	@GetMapping("/{orderId}")
	@Operation(summary = "Get Order by orderId")
	public ResponseEntity<Order> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws UserException, OrderException{
		User user = userService.findUserByJwtToken(jwt);
		
		return ResponseEntity.ok(orderService.findOrderById(orderId));
	}
	
	@GetMapping("/item/{orderItemId}")
	@Operation(summary = "Get Order item by orderItemId")
	public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId, @RequestHeader("Authorization") String jwt) throws UserException, OrderException{
		User user = userService.findUserByJwtToken(jwt);
		
		return ResponseEntity.ok(orderService.findByOrderItemId(orderItemId));
	}
	
	@PutMapping("/{orderId}/cancel")
	@Operation(summary = "Cancel Order")
	public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws UserException, OrderException, SellerException{
		User user = userService.findUserByJwtToken(jwt);
		Order order = orderService.cancelOrder(orderId, user);
		
		Seller seller = sellerService.getSellerById(order.getSellerId());
		SellerReport report = sellerReportService.getSellerReport(seller);
		
		report.setCancelledOrders(report.getCancelledOrders());
		report.setTotalRefunds(report.getTotalRefunds()+order.getTotalSellingPrice());
		sellerReportService.updateSellerReport(report);
		
		return ResponseEntity.ok(order);
	}
}
