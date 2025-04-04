package com.registration.services;

import java.util.List;

import com.registration.entities.User;

public interface UserService {
	
	List<User> get();
	
	User get(Long userId);

	User registerUser(User user);
}
