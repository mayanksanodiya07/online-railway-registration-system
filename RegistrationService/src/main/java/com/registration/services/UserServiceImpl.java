package com.registration.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.registration.entities.User;
import com.registration.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;
	
//	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	@Override
	public User registerUser(User user) {
		Optional<User> existingUser = userRepository.findByEmail(user.getEmail());	
		
		if(existingUser.isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		
//		user.setPassword(encoder.encode(user.getPassword()));
		
		return userRepository.save(user);
	}

	@Override
	public List<User> get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User get(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
