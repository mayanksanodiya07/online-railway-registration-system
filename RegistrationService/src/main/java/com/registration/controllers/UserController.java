package com.registration.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.entities.User;
import com.registration.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userServive;
	
	public UserController(UserService userServive) {
		this.userServive = userServive;
	}
	
	@PostMapping
	public User create(@RequestBody User user) {
		return userServive.registerUser(user);
	}
}
