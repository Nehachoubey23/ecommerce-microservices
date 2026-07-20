package com.ecommerce.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {
	@Id
	private String id;
	private String street;
	private String city;
	private String country;
	private String zipcode;
	
	 @PrePersist
	    public void generateId() {
	        if (id == null || id.isBlank()) {
	            id = java.util.UUID.randomUUID().toString();
	        }
	    }
}
