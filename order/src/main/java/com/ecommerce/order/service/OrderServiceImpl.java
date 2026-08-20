package com.ecommerce.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.order.client.UserClient;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final CartService cartService;
	private final UserClient userClient;

	@Override
	public Optional<OrderResponse> createOrder(String userId) {
		// TODO Auto-generated method stub
		List<CartItem> cartItems = cartService.fetchAllCarts(userId);
		if (cartItems.isEmpty()) {
			return Optional.empty();
		}
		  UserResponse user;
	        try {
	            user = userClient.getUser(userId);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
		BigDecimal totalPrice = cartItems.stream().
				map(CartItem::getPrice).
				reduce(BigDecimal.ZERO, BigDecimal::add);
		
		Order order = new Order();
		 order.setUserId(user.getId());
		order.setStatus(OrderStatus.CONFIRMED);
		order.setTotalAmount(totalPrice);
		 List<OrderItem> orderItems = cartItems.stream()
		            .map(item -> new OrderItem(
		                    null,                    // id
		                    item.getProductId(),     // productId
		                    item.getQuantity(),      // quantity
		                    item.getPrice(),         // price
		                    order                    // order
		            ))
		            .toList();

		    order.setItems(orderItems);

		    // Save order
		    Order savedOrder = orderRepository.save(order);
		cartService.clearCart(userId);
		return Optional.of(maptoOrderResponse(savedOrder));
	}
	private OrderResponse maptoOrderResponse(Order savedorder) {

	    return new OrderResponse(
	            savedorder.getId(),
	            savedorder.getTotalAmount(),   // 2nd parameter
	            savedorder.getStatus(),        // 3rd parameter
	            savedorder.getItems().stream()
	                    .map(item -> new OrderItemDTO(
	                            item.getId(),
	                            item.getProductId(),
	                            item.getQuantity(),
	                            item.getPrice(),
	                            item.getPrice()
	                    ))
	                    .toList(),
	            savedorder.getCreatedAt()
	    );
	}
	

}
