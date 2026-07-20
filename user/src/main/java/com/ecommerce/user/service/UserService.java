package com.ecommerce.user.service;


import java.util.List;
import java.util.Optional;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;

public interface UserService {

	List<UserResponse> fetchAllUsers();

	void create(UserRequest userrequest);

	boolean editUserById(String id, UserRequest updatedUser);

	Optional<UserResponse> fetchUserById(String id);

}
