package com.ecommerce.order.service;

import java.util.List;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;

public interface CartService {

	public boolean addToCart(String userId, CartItemRequest request);

	public boolean deleteItemFromCart(String userId, String productId);

	List<CartItem> fetchAllCarts(String userId);

	public void clearCart(String userId);

}
