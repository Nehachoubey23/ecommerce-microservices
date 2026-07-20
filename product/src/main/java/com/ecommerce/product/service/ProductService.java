package com.ecommerce.product.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.dto.ProductResquest;

public interface ProductService {

	ProductResponse create(ProductResquest productrequest);

	Optional<ProductResponse> updateProduct(String id, ProductResquest productrequest);

	List<ProductResponse> fetchAllProducts();

	boolean DeletedProducts(String id);

	List<ProductResponse> searchProducts(String keyword);

	Optional<ProductResponse> getProductById(String id);

}
