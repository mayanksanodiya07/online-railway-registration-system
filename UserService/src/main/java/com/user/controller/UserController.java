package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.user.entity.User;
import com.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Get currently logged-in user (requires token validation)
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser( ) {
        User user = userService.getCurrentUser( );
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestHeader("Authorization") String token ) {
    	
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<Object>> getUserBookings(@PathVariable Long id, @RequestHeader("Authorization") String token) {
         
    	System.out.println(id);
        List<Object> bookings = userService.getBookingsByUserId(id, token);
        return ResponseEntity.ok(bookings);
    }
}
