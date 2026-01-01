package com.ecommerce.service.impl;

import org.springframework.stereotype.Service;

import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.User;
import com.ecommerce.exception.CartItemException;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.service.CartItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
	
	private final CartItemRepository cartItemRepository;
	private final CartItemRepository cartRepository;

	@Override
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException {
		CartItem item = findCartItemById(id);
		
		User cartItemUser = item.getCart().getUser();
		
		if(cartItemUser.getId().equals(userId)) {
			item.setQuantity(cartItem.getQuantity());
			item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
			item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
			return cartItemRepository.save(item);
		}
		throw new CartItemException("you can't update this cartItem");
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException {
		CartItem cartItem = findCartItemById(cartItemId);
		
		User cartItemUser = cartItem.getCart().getUser();
		
		if(cartItemUser.getId().equals(userId)) {
			cartItemRepository.delete(cartItem);
		}else {
			throw new CartItemException("You can't delete this item...");
		}
		
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		return cartItemRepository.findById(cartItemId).orElseThrow(() -> new CartItemException("cart item not found with id: " + cartItemId));
	}

}
