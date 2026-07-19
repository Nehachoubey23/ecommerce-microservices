package com.ecommerce.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cart")
public class CartController {

	private final CartService cartService;

	@PostMapping
	public ResponseEntity<String> addToCart(@RequestHeader("X-USER-ID") String userId,
			@RequestBody CartItemRequest request) {
		if (!cartService.addToCart(userId, request)) {
			return ResponseEntity.badRequest().body("Product Out of Stock or User not found");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	@DeleteMapping("/items/{productId}")
	public ResponseEntity<Void> removeFromCart(@RequestHeader("X-USER-ID") String userId,
			@PathVariable String productId) {
		boolean deleted = cartService.deleteItemFromCart(userId, productId);

		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

	}

	@GetMapping
	public ResponseEntity<List<CartItem>> cartList(@RequestHeader("X-USER-ID") String userId) {

		return ResponseEntity.ok(cartService.fetchAllCarts(userId));

	}

}