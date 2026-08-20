package com.ecommerce.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ecommerce.user.dto.AddressDto;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	//private List<User> userList = new ArrayList<>();
	private final UserRepository userRepository;

	@Override
	@Cacheable(value = "users", key = "'allUsers'")
	public List<UserResponse> fetchAllUsers() {
		return userRepository.findAll().stream().map(this::mapToUserResponse).collect(Collectors.toList());
	}

	@Override
	@CacheEvict(value = "users", key = "'allUsers'")
	public void create(UserRequest userrequest) {
		
		 User user = new User();
		 updateUserFromRequest(user,userrequest);
		 userRepository.save(user);
		   
	}

	private void updateUserFromRequest(User user, UserRequest userrequest) {
		// TODO Auto-generated method stub
	   user.setFirstName(userrequest.getFirstName());
       user.setLastName(userrequest.getLastName());
       user.setEmail(userrequest.getEmail());
       user.setPhone(userrequest.getPhone());
       if(userrequest.getAddress() != null) {
    	   Address address =  new Address();
    	   address.setStreet(userrequest.getAddress().getStreet());
    	   address.setCity(userrequest.getAddress().getCity());
    	   address.setCountry(userrequest.getAddress().getCountry());
    	   address.setZipcode(userrequest.getAddress().getZipcode());
			user.setAddress(address);
			
    	   
       }
		
	}

	@Override
	@Cacheable(value = "users", key = "#id")
	public Optional<UserResponse> fetchUserById(String id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id).map(this::mapToUserResponse);
	
		/*
		 * return userList.stream().filter(user ->
		 * user.getId().equals(id)).findFirst().map(ResponseEntity::ok)
		 * .orElse(ResponseEntity.notFound().build());
		 */
	}

	@Override
	@CacheEvict(value = "users", key = "#id")
	public boolean editUserById(String id, UserRequest updatedUser) {
		// TODO Auto-generated method stub
		return userRepository.findById(id) .map(existingUser -> {
			updateUserFromRequest(existingUser,updatedUser);
			userRepository.save(existingUser);
			return true;

		}).orElse(false);
	}
	
	private UserResponse mapToUserResponse(User user) {
		UserResponse userresponse = new UserResponse();
		userresponse.setId(String.valueOf(user.getId()));
		userresponse.setFirstName(user.getFirstName());
		userresponse.setLastName(user.getLastName());
		userresponse.setEmail(user.getEmail());
		userresponse.setPhone(user.getPhone());
		userresponse.setRole(user.getRole());
		if(user.getAddress() != null) {
			AddressDto addressDto =  new AddressDto();
			addressDto.setStreet(user.getAddress().getStreet());
			addressDto.setCity(user.getAddress().getCity());
			addressDto.setCountry(user.getAddress().getCountry());
			addressDto.setZipcode(user.getAddress().getZipcode());
			userresponse.setAddress(addressDto);
		}
		return userresponse;
	}

	
	
}