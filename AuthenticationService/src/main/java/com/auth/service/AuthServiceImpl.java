package com.auth.service;

import com.auth.dto.LoginResponse;
import com.auth.dto.UserDTO;
import com.auth.entity.User;
import com.auth.exception.DuplicateUserException;
import com.auth.exception.ExternalServiceException;
import com.auth.exception.InvalidCredentialsException;
import com.auth.exception.InvalidPasswordException;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    
	@Autowired
    private UserRepository userRepository;
     
    @Autowired
    UserClient userClient;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User registerUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUserException("Username already exists");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new InvalidPasswordException("Password must be at least 6 characters long, include a number, a special character, an uppercase letter, and a lowercase letter");
        }

        user.setRoles(List.of("USER"));
        user.setPassword(encoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        
        String jwt = jwtUtil.generateInternalToken(savedUser.getId(), savedUser.getUsername(), savedUser.getRoles());

        UserDTO dto = new UserDTO();
        dto.setUsername(savedUser.getUsername());
        dto.setRoles(savedUser.getRoles());
        dto.setAuthUserId(savedUser.getId());

        try {
            userClient.createUser(dto, "Bearer " + jwt);
        } catch (Exception ex) {
            userRepository.deleteById(savedUser.getId());
            throw new ExternalServiceException("Failed to create user in User Service. Please try again later.");
        }

        return savedUser;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public LoginResponse login(String username, String password) { 
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByUsername(username);

            String token = jwtUtil.generateToken(user.getId(), username, user.getRoles());
            
            return new LoginResponse(user.getId(), token); 

        } catch (BadCredentialsException e) {
        	throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    
    @Override
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }

        userRepository.delete(user);
    }

    private boolean isValidPassword(String password) {
        return password != null &&
               password.length() >= 6 &&
               password.matches(".*\\d.*") &&                      // at least one digit
               password.matches(".*[!@#$%^&*()].*") &&             // at least one special character
               password.matches(".*[a-z].*") &&                    // at least one lowercase letter
               password.matches(".*[A-Z].*");                      // at least one uppercase letter
    }
}
