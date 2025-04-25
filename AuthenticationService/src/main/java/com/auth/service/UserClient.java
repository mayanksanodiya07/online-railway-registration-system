package com.auth.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.auth.dto.UserDTO;

import feign.FeignException;

@FeignClient(url = "http://localhost:8082", name = "USER-SERVICE")
public interface UserClient {

    @PostMapping("/users")
    default UserDTO createUser(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token) {
        try {
            UserDTO response = this.createUserRequest(userDTO, token); 
            return response;
        } catch (FeignException e) {
            
            throw new RuntimeException("Failed to create user in external service.", e); // Re-throwing the exception after logging
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred while creating user.", ex); // Re-throwing the exception
        }
    }
    @PostMapping("/users")
    UserDTO createUserRequest(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token);
}
