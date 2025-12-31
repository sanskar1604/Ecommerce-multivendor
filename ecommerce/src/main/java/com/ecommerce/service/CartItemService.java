package com.ecommerce.service;

import com.ecommerce.entity.CartItem;
import com.ecommerce.exception.CartItemException;

public interface CartItemService {

	CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException;
	
	void removeCartItem(Long userId, Long cartItemId);
	
	CartItem findCartItemById(Long cartItemId);
}
