package com.ecommerce.order.service;

import org.springframework.stereotype.Service;

import com.ecommerce.order.client.UserClient;
import com.ecommerce.order.dto.UserResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResilientUserClient {

	private final UserClient userClient;

	@CircuitBreaker(name = "userService", fallbackMethod = "userFallback")
	public UserResponse getUser(String userId) {

		System.out.println("Calling User Service: " + userId);

		return userClient.getUser(userId);
	}

	public UserResponse userFallback(String userId, Throwable throwable) {

		System.out.println("User Service Circuit Breaker FALLBACK");

		System.out.println("User ID: " + userId);

		System.out.println("Reason: " + throwable.getMessage());

		return null;
	}
}
