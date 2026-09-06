package com.ecommerce.order.service;

import org.springframework.stereotype.Service;

import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.dto.ProductResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResilientProductClient {
	private final ProductClient productClient;

	 @Retry(
	            name = "productService"
	    )
	@CircuitBreaker(name = "productService", fallbackMethod = "productFallback")
	public ProductResponse getProduct(String productId) {

		return productClient.getProduct(productId);
	}

	public ProductResponse productFallback(String productId, Throwable throwable) {

		System.out.println("Product Service is unavailable");
		System.out.println("Product ID: " + productId);
		System.out.println("Reason: " + throwable.getMessage());

		return null;
	}

}
