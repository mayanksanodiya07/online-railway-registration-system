package com.auth.service;

import java.util.List;

import com.auth.dto.LoginResponse;
import com.auth.entity.User;

public interface AuthService {

    User registerUser(User user);
    
    List<User> getUsers();
    
    LoginResponse login(String username, String password);

	void deleteUserByUsername(String username);
    
    
}
