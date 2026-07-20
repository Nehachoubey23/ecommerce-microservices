package com.ecommerce.product.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.dto.ProductResquest;
import com.ecommerce.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {

	private final ProductService productService;
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {

	    return productService.getProductById(id)
	            .map(ResponseEntity::ok)
	            .orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductResquest productrequest) {

		return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productrequest));

	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable String id,
			@RequestBody ProductResquest productrequest) {

		return productService.updateProduct(id, productrequest).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> fetchProducts() {

		return ResponseEntity.ok(productService.fetchAllProducts());

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> DeleteProduct(@PathVariable String id) {

		boolean isDeleted = productService.DeletedProducts(id);

		return isDeleted ? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ProductResponse>> DeleteProductBySerach(@RequestParam  String keyword) {
       return  ResponseEntity.ok(productService.searchProducts(keyword));
	}
}