package com.ecommerce.service.impl;

import org.springframework.stereotype.Service;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	@Override
	public CartItem addCartItem(User user, Product product, String size, int quantity) {
		Cart cart = findUserCart(user);
		
		CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
		
		if(isPresent == null) {
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItem.setUserId(user.getId());
			cartItem.setSize(size);
			
			int totalPrice = quantity * product.getSellingPrice();
			cartItem.setSellingPrice(totalPrice);
			
			cart.getCartItems().add(cartItem);
			cartItem.setCart(cart);
			return cartItemRepository.save(cartItem);
		}
		return isPresent;
	}

	@Override
	public Cart findUserCart(User user) {
		Cart cart = cartRepository.findByUserId(user.getId());
		
		int totalPrice = 0;
		int totalDiscountPrice = 0;
		int totalItem = 0;
		
		for(CartItem cartItem: cart.getCartItems()) {
			totalPrice += cartItem.getMrpPrice();
			totalDiscountPrice += cartItem.getSellingPrice();
			totalItem += cartItem.getQuantity();
		}
		
		cart.setTotalMrpPrice(totalPrice);
		cart.setTotalItem(totalItem);
		cart.setTotalSellingPrice(totalDiscountPrice);
		cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountPrice));

		return cart;
	}
	
	private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
		if(mrpPrice <= 0) {
			throw new IllegalArgumentException("Actual price must be greater than 0");
		}
		double discount = mrpPrice-sellingPrice;
		double discountPercentage = (discount/mrpPrice)*100;
		return (int)discountPercentage;
	}
}
