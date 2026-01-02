package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.OrderStatus;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Seller;
import com.ecommerce.exception.OrderException;
import com.ecommerce.exception.SellerException;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.SellerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sellers/orders")
@RequiredArgsConstructor
@Tag(name = "Seller order", description = "API for managing sellers order")
public class SellerOrderController {

	private final OrderService orderService;
	private final SellerService sellerService;
	
	@GetMapping
	@Operation(summary = "Get all orders")
	public ResponseEntity<List<Order>> getAllOrdersHandler(@RequestHeader("Authorization") String jwt) throws SellerException{
		Seller seller = sellerService.getSellerProfile(jwt);
		List<Order> orders = orderService.sellersOrder(seller.getId());
		
		return ResponseEntity.ok(orders);
	}
	
	@PutMapping("/{orderId}/status/{orderStatus}")
	@Operation(summary = "Update order status")
	public ResponseEntity<Order> updateOrderHandler(@RequestHeader("Authorization") String jwt,
			@PathVariable Long orderId,
			@PathVariable OrderStatus orderStatus) throws OrderException{
		Order order = orderService.updateOrderStatus(orderId, orderStatus);
		
		return ResponseEntity.ok(order);
	}
	
}
