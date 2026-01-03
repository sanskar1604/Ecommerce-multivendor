package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Wishlist;

public interface WishlistService {

	Wishlist createWishlist(User user);
	
	Wishlist getWishlistByUserId(User user);
	
	Wishlist addProductToWishlist(User user, Product product);
}
