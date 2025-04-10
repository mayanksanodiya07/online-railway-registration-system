package com.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.dto.LoginRequest;
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
    @GetMapping("/getAll")
    public List<User> getAll() {
      
      return authService.getUsers();
  }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
    	
        String token = authService.login(request.getUsername(), request.getPassword());
        
        return ResponseEntity.ok(token);
    }

//    @GetMapping("/validate")
//    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
//        boolean isValid = AuthService.validateToken(token);
//        return ResponseEntity.ok(isValid);
//    }
}
