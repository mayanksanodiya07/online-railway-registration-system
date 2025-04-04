package com.auth.service;

import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // **Register a new user**
    public User registerUser(User user) {
    	user.setRole(Role.USER);
    	user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public List<User> getUsers() {
    	return userRepository.findAll();
    }
    
    // **Authenticate user and generate JWT**
    public String login(String username, String password) {
    	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    	SecurityContextHolder.getContext().setAuthentication(authentication);
    	User user = userRepository.findByUsername(username);
        return jwtUtil.generateToken(username, user.getRole().name());
    }

//     **Validate JWT Token**
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
//
//    // **Extract username from JWT**
//    public String extractUsername(String token) {
//        return jwtUtil.extractUsername(token);
//    }
}
