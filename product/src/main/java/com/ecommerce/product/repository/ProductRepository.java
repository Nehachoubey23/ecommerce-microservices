package com.ecommerce.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.product.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	List<Product> findByActiveTrue();

	@Query("SELECT p FROM products p  WHERE p.active = true  AND p.stockQuantity > 0  AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Product> searchProducts(@Param("keyword") String keyword);

}
