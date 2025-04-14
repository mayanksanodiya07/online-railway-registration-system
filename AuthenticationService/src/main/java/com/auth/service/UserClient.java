package com.auth.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.auth.dto.UserDTO;
   

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import feign.FeignException;

@FeignClient(url = "http://localhost:8082", name = "USER-SERVICE")
public interface UserClient {

    Logger logger = LoggerFactory.getLogger(UserClient.class);

    @PostMapping("/users")
    default UserDTO createUser(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token) {
        try {
            logger.info("Attempting to create user in USER-SERVICE: {}", userDTO.getUsername());
            UserDTO response = this.createUserRequest(userDTO, token); 
            logger.info("Successfully created user in USER-SERVICE: {}", userDTO.getUsername());
            return response;
        } catch (FeignException e) {
            logger.error("Failed to create user in USER-SERVICE: {}. Status: {}. Message: {}", 
                         userDTO.getUsername(), e.status(), e.getMessage());
            throw new RuntimeException("Failed to create user in external service.", e); // Re-throwing the exception after logging
        } catch (Exception ex) {
            logger.error("Unexpected error occurred while creating user in USER-SERVICE: {}. Message: {}", 
                         userDTO.getUsername(), ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error occurred while creating user.", ex); // Re-throwing the exception
        }
    }
    @PostMapping("/users")
    UserDTO createUserRequest(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token);
}
