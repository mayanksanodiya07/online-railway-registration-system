package com.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.user.client.AuthClient;
import com.user.client.BookingClient;
import com.user.entity.User;
import com.user.exception.UserNotFoundException;
import com.user.repository.UserRepository;
import com.user.util.JwtUtil;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingClient bookingClient;

    @Autowired
    private AuthClient authClient;
    
    @Autowired
    JwtUtil jwtUtil;
    
    @Override
    public User createUser(User user) {
        
        return userRepository.save(user);
    }
    
//    @Override
//    public User getUserById(Long id) {
//        return userRepository.findById(id)
//            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
//    }

    @Override
    public User getCurrentUser( ) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	String username = authentication.getName(); 
        
    	return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    @Override
    public User updateUser(User updatedUser) {
        User user = getCurrentUser();

        if (updatedUser.getFullName() != null) {
        	user.setFullName(updatedUser.getFullName());
        }

        if (updatedUser.getEmail() != null) {
        	user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPhoneNumber() != null) {
        	user.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        return userRepository.save(user);
    }


    @Override
    public void deleteUser() {
    	User user = getCurrentUser();    	

        String jwt = jwtUtil.generateInternalToken(user.getUsername(), user.getRoles());

    	authClient.deleteUser(user.getUsername(), "Bearer " + jwt);
        userRepository.deleteById(user.getId());
    }

    @Override
    public List<Object> getBookingsByUserId(String token) {
    	User user = getCurrentUser();  
        return bookingClient.getBookingsByUserId(user.getId() , token );
    }

}
