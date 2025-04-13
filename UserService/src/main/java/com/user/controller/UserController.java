package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws AccessDeniedException {
        User user = userService.getUserById(id);
        validateOwnership(user);
        return ResponseEntity.ok(user);
    }

    // Get currently logged-in user (requires token validation)
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser( ) throws AccessDeniedException {
        User user = userService.getCurrentUser( );
        validateOwnership(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestHeader("Authorization") String token ) {
    	
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) throws AccessDeniedException {
    	User user = userService.getUserById(id);
    	validateOwnership(user);
        User updated = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws AccessDeniedException {
    	User user = userService.getUserById(id);
    	validateOwnership(user);
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<Object>> getUserBookings(@PathVariable Long id, @RequestHeader("Authorization") String token) throws AccessDeniedException {
    	User user = userService.getUserById(id);
    	validateOwnership(user);
        List<Object> bookings = userService.getBookingsByUserId(id, token);
        return ResponseEntity.ok(bookings);
    }
    
    private void validateOwnership(User user) throws AccessDeniedException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!user.getUsername().equals(username)) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }
    }

}
