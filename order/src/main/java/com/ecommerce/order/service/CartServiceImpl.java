package com.ecommerce.order.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.client.UserClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

	private final CartItemRepository cartItemRepository;
	private final ProductClient productClient;
	private final UserClient userClient;

	@Override
	@CacheEvict(value = "carts", key = "#userId")
	public boolean addToCart(String userId, CartItemRequest request) {

	    System.out.println("=== ADD TO CART ===");
	    System.out.println("User ID: " + userId);
	    System.out.println("Product ID: " + request.getProductId());
	    System.out.println("Quantity: " + request.getQuantity());

	    ProductResponse product;
	    try {
	        product = productClient.getProduct(request.getProductId());

	        System.out.println("Product fetched successfully");
	        System.out.println("Stock: " + product.getStockQuantity());

	    } catch (Exception e) {
	        System.out.println("Product service call failed");
	        e.printStackTrace();
	        return false;
	    }

	    // Check stock
	    if (product.getStockQuantity() < request.getQuantity()) {
	        System.out.println("Stock check failed");
	        return false;
	    }

	    UserResponse user;
	    try {
	        user = userClient.getUser(userId);

	        System.out.println("User fetched successfully: " + user.getId());

	    } catch (Exception e) {
	        System.out.println("User service call failed");
	        e.printStackTrace();
	        return false;
	    }

	    CartItem existingCartItem =
	            cartItemRepository.findByUserIdAndProductId(
	                    user.getId(),
	                    product.getId());

	    if (existingCartItem != null) {

	        int newQuantity =
	                existingCartItem.getQuantity() + request.getQuantity();

	        existingCartItem.setQuantity(newQuantity);

	        existingCartItem.setPrice(
	                product.getPrice()
	                       .multiply(BigDecimal.valueOf(newQuantity)));

	        cartItemRepository.save(existingCartItem);

	    } else {

	        CartItem cartItem = new CartItem();

	        cartItem.setUserId(user.getId());

	        cartItem.setProductId(product.getId());

	        cartItem.setQuantity(request.getQuantity());

	        cartItem.setPrice(
	                product.getPrice()
	                       .multiply(BigDecimal.valueOf(request.getQuantity())));

	        cartItemRepository.save(cartItem);
	    }

	    System.out.println("Item added successfully");

	    return true;
	}

	@Override
	@CacheEvict(value = "carts", key = "#userId")
	public boolean deleteItemFromCart(String userId, String productId) {
		// TODO Auto-generated method stub
		 try {
		        userClient.getUser(userId);
		    } catch (Exception e) {
		        return false; // user not found
		    }

		    CartItem cartItem =
		            cartItemRepository.findByUserIdAndProductId(userId, productId);

		    if (cartItem == null) {
		        return false;
		    }

		    // Delete item
		    cartItemRepository.delete(cartItem);

		    return true;
	}
	
	@Override
	@Cacheable(value = "carts", key = "#userId")
	public List<CartItem> fetchAllCarts(String userId) {
		// TODO Auto-generated method stub
		 // Optional: validate user through User Service
	    try {
	        userClient.getUser(userId);
	    } catch (Exception e) {
	        return List.of(); // user not found
	    }

	    // Fetch all cart items for this user
	    return cartItemRepository.findByUserId(userId);
	}

	@Override
	@CacheEvict(value = "carts", key = "#userId")
	public void clearCart(String userId) {
		// TODO Auto-generated method 
		  cartItemRepository.deleteByUserId(userId);
		  }
}
