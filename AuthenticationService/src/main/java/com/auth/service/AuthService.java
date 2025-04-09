package com.auth.service;

import java.util.List;

import com.auth.entity.User;

public interface AuthService {

    User registerUser(User user);
    
    List<User> getUsers();
    
    String login(String username, String password);
}
