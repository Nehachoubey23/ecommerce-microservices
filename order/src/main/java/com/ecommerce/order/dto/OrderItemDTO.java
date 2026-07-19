package com.ecommerce.order.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {

	private Long id;
	private String productId;
	private Integer quantity;
	private BigDecimal price;
	private BigDecimal subtotal;
}
