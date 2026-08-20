package com.ecommerce.product.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.dto.ProductResquest;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	/*
	 * @Override
	 * 
	 * @CachePut(value = "products", key = "#result.id") public ProductResponse
	 * create(ProductResquest productrequest) { // TODO Auto-generated method stub
	 * Product product = new Product(); updateProductfromRequest(product,
	 * productrequest); Product savedProduct = productRepository.save(product);
	 * 
	 * return mapToProductResponse(savedProduct); }
	 */

	private ProductResponse mapToProductResponse(Product savedProduct) {
		// TODO Auto-generated method stub
		ProductResponse response = new ProductResponse();
		response.setId(String.valueOf(savedProduct.getId()));
		response.setCategory(savedProduct.getCategory());
		response.setDescription(savedProduct.getDescription());
		response.setName(savedProduct.getName());
		response.setPrice(savedProduct.getPrice());
		response.setImageUrl(savedProduct.getImageUrl());
		response.setActive(savedProduct.getActive());
		response.setStockQuantity(savedProduct.getStockQuantity());
		return response;
	}

	private void updateProductfromRequest(Product product, ProductResquest productrequest) {
		// TODO Auto-generated method stub
		product.setCategory(productrequest.getCategory());
		product.setDescription(productrequest.getDescription());
		product.setName(productrequest.getName());
		product.setPrice(productrequest.getPrice());
		product.setImageUrl(productrequest.getImageUrl());
		product.setStockQuantity(productrequest.getStockQuantity());

	}

	@Override
	@CachePut(value = "products", key = "#id")
	public Optional<ProductResponse> updateProduct(String id, ProductResquest productrequest) {
		// TODO Auto-generated method stub

		return productRepository.findById(id).map(existingProduct -> {
			updateProductfromRequest(existingProduct, productrequest);
			Product savedProduct = productRepository.save(existingProduct);

			return mapToProductResponse(savedProduct);
		});

	}

	@Cacheable(value = "products", key = "'all'")
	@Override
	public List<ProductResponse> fetchAllProducts() {
		// TODO Auto-generated method stub
		System.out.println("===== FETCHING PRODUCTS FROM DATABASE =====");

		return productRepository.findByActiveTrue().stream().map(this::mapToProductResponse)
				.collect(Collectors.toList());
	}

	@Override
	@CacheEvict(value = "products", key = "#id")
	public boolean DeletedProducts(String id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).map(product -> {
			product.setActive(false);
			productRepository.save(product);
			return true;
		}).orElse(false);

	}

	@Override
	@Cacheable(value = "productSearch", key = "#keyword")
	public List<ProductResponse> searchProducts(String keyword) {
		// TODO Auto-generated method stub
		return productRepository.searchProducts(keyword).stream().map(this::mapToProductResponse)
				.collect(Collectors.toList());
	}

	@Cacheable(value = "products", key = "#id")
	@Override
	public Optional<ProductResponse> getProductById(String id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).map(this::mapToProductResponse);
	}

	@Override
	public List<ProductResponse> createProducts(List<ProductResquest> productRequests) {
		// TODO Auto-generated method stub
		List<Product> products = productRequests.stream()
	            .map(request -> {
	                Product product = new Product();
	                updateProductfromRequest(product, request);
	                return product;
	            })
	            .collect(Collectors.toList());

	    List<Product> savedProducts = productRepository.saveAll(products);

	    return savedProducts.stream()
	            .map(this::mapToProductResponse)
	            .collect(Collectors.toList());
	}

}
