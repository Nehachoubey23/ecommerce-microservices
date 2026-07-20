package com.ecommerce.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/api/users")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
	}

	@PostMapping("/api/users/create")
	public ResponseEntity<String> createUsers(@RequestBody UserRequest userrequest) {
		System.out.println("User = " + userrequest);
		System.out.println("Address = " + userrequest.getAddress());

		userService.create(userrequest);
		return ResponseEntity.status(HttpStatus.CREATED).body("User added sucessFully.");
	}

	@GetMapping("/api/users/{id}")
	public Optional<UserResponse> checkUser(@PathVariable String id) {
		return userService.fetchUserById(id);
	}

	@PutMapping("/api/users/{id}")
	public ResponseEntity<String> editUser(@PathVariable String id, @RequestBody UserRequest updatedUser) {
		boolean updated = userService.editUserById(id, updatedUser);
		if (updated)
			return ResponseEntity.status(HttpStatus.OK).body("User updated sucessFully.");
		return ResponseEntity.notFound().build();
	}
}