package com.ecommerce.service;

import java.util.List;
import java.util.Set;

import com.ecommerce.domain.OrderStatus;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.User;
import com.ecommerce.exception.OrderException;
import com.ecommerce.exception.UserException;

public interface OrderService {

	Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
	
	Order findOrderById(Long orderId) throws OrderException;
	
	List<Order> userOrderHistory(Long userId);
	
	List<Order> sellersOrder(Long sellerId);
	
	Order updateOrderStatus(Long orderId, OrderStatus status) throws OrderException;
	
	Order cancelOrder(Long orderId, User user) throws OrderException, UserException;
	
	OrderItem findByOrderItemId(Long orderItemId) throws OrderException;
}
