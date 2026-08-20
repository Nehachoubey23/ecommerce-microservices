package com.ecommerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.order.dto.UserResponse;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserClient {

	  @GetMapping("/api/users/{id}")
	    UserResponse getUser(@PathVariable("id") String id);
}
