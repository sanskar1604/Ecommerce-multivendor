package com.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommerce.domain.OrderStatus;
import com.ecommerce.domain.PaymentStatus;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.User;
import com.ecommerce.exception.OrderException;
import com.ecommerce.exception.UserException;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	
	private final OrderRepository orderRepository;
	private final AddressRepository addressRepository;
	private final OrderItemRepository orderItemRepository;

	@Override
	public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
		if(!user.getAddresses().contains(shippingAddress)) {
			user.getAddresses().add(shippingAddress);
		}
		
		addressRepository.save(shippingAddress);
		
		Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
				.collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));
		Set<Order> orders = new HashSet<>();
		
		for(Map.Entry<Long, List<CartItem>> entry: itemsBySeller.entrySet()) {
			Long sellerId = entry.getKey();
			List<CartItem> items = entry.getValue();
			
			int totalOrderPrice = items.stream().mapToInt(
					CartItem::getSellingPrice).sum();
			int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();
			
			
			Order createdOrder = new Order();
			createdOrder.setUser(user);
			createdOrder.setSellerId(sellerId);
			createdOrder.setTotalMrpPrice(totalOrderPrice);
			createdOrder.setTotalSellingPrice(totalOrderPrice);
			createdOrder.setTotalItem(totalItem);
			createdOrder.setShippingAddress(shippingAddress);
			createdOrder.setOrderStatus(OrderStatus.PENDING);
			createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
			
			Order savedOrder = orderRepository.save(createdOrder);
			orders.add(savedOrder);
			
			List<OrderItem> orderItems = new ArrayList<>();
			
			for(CartItem item: items) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrder(savedOrder);
				orderItem.setMrpPrice(item.getMrpPrice());
				orderItem.setProduct(item.getProduct());
				orderItem.setQuantity(item.getQuantity());
				orderItem.setSize(item.getSize());
				orderItem.setUserId(item.getUserId());
				orderItem.setSellingPrice(item.getSellingPrice());
				
				savedOrder.getOrderItems().add(orderItem);
				
				OrderItem savedOrderItem = orderItemRepository.save(orderItem);
				
				orderItems.add(savedOrderItem);
			}
		}
		return orders;
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		return orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order not found with id: " + orderId));
	}

	@Override
	public List<Order> userOrderHistory(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	@Override
	public List<Order> sellersOrder(Long sellerId) {
		return orderRepository.findBySellerId(sellerId);
	}

	@Override
	public Order updateOrderStatus(Long orderId, OrderStatus status) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(status);
		return orderRepository.save(order);
	}

	@Override
	public Order cancelOrder(Long orderId, User user) throws OrderException, UserException {
		Order order = findOrderById(orderId);
		
		if(!user.getId().equals(order.getUser().getId())) {
			throw new UserException("you don't have access to this order...");
		}
		
		order.setOrderStatus(OrderStatus.CANCELLED);
		return orderRepository.save(order);
	}

	@Override
	public OrderItem findByOrderItemId(Long orderItemId) throws OrderException {
		return orderItemRepository.findById(orderItemId).orElseThrow(() -> new OrderException("Order Item not found with id: " + orderItemId));
	}

}
