package com.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.entity.User;
import com.auth.service.AuthServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
    	
        authService.registerUser(user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    	
        LoginResponse token = authService.login(request.getUsername(), request.getPassword());
        
        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username, @RequestHeader("Authorization") String token) {
        
        authService.deleteUserByUsername(username);
        return ResponseEntity.ok("User deleted from AuthService");
    }

    @GetMapping("/getAll")
    public List<User> getAll() {
        return authService.getUsers();
    }
}
