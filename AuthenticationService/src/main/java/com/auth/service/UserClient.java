package com.auth.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.auth.dto.UserDTO;
  

@FeignClient(url="http://localhost:8082", name = "USER-SERVICE")
public interface UserClient {

    @PostMapping("/users")
    UserDTO createUser(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token);
}
