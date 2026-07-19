package com.ecommerce.order.service;

import java.util.Optional;

import com.ecommerce.order.dto.OrderResponse;

public interface OrderService {

	Optional<OrderResponse> createOrder(String userId);

}
