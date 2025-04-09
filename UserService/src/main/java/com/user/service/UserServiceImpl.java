package com.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.user.client.BookingClient;
import com.user.entity.User;
import com.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingClient bookingClient;
    
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public User getCurrentUser( ) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	String username = authentication.getName(); 
        
    	return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public User createUser(User user) {
        
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);

        if (updatedUser.getFullName() != null) {
            existing.setFullName(updatedUser.getFullName());
        }

        if (updatedUser.getEmail() != null) {
            existing.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPhoneNumber() != null) {
            existing.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        return userRepository.save(existing);
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Object> getBookingsByUserId(Long id, String token) {
    	
        return bookingClient.getBookingsByUserId(id , token );
    }

    // Placeholder - you can implement JWT decoding or call AuthService
//    private String extractUsernameFromToken(String token) {
//        // remove "Bearer " prefix
//        token = token.replace("Bearer ", "");
//        // decode JWT token and extract username
//        // You can use JWT library (e.g., io.jsonwebtoken.Jwts) or call /validate from Auth Service
//        return "decodedUsername";
//    }
}
