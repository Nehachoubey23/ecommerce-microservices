package com.ecommerce.product.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductResponse {
	
	private String id;
	private String name;
	private String description;
	private BigDecimal  price;
	private  String category;
	private  String imageUrl;
	private Integer stockQuantity;
	private Boolean active = true;

}
