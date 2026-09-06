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
	private final ResilientProductClient resilientProductClient;
	private final ResilientUserClient resilientUserClient;
	@Override
	@CacheEvict(value = "carts", key = "#userId")
	public boolean addToCart(String userId, CartItemRequest request) {

	    System.out.println("=== ADD TO CART ===");
	    System.out.println("User ID: " + userId);
	    System.out.println("Product ID: " + request.getProductId());
	    System.out.println("Quantity: " + request.getQuantity());

	    ProductResponse product =
				resilientProductClient.getProduct(
                        request.getProductId());

        if (product == null) {

            System.out.println(
                    "Product Service unavailable");

            return false;
        }


        System.out.println(
                "Product fetched successfully");
        System.out.println(
                "Stock: " + product.getStockQuantity());
	    if (product.getStockQuantity() < request.getQuantity()) {
	        System.out.println("Stock check failed");
	        return false;
	    }
	    UserResponse user =
                resilientUserClient.getUser(userId);

        if (user == null) {

            System.out.println(
                    "User Service unavailable");

            return false;
        }


        System.out.println(
                "User fetched successfully: "
                + user.getId());


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
		 UserResponse user =
	                resilientUserClient.getUser(userId);
		 if (user == null) {
	            return false;
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
		  UserResponse user =
	                resilientUserClient.getUser(userId);

		  if (user == null) {
	            return List.of();
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
