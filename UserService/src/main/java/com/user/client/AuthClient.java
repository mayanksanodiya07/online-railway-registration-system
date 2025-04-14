package com.user.client;
 
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url="http://localhost:8081", name="AUTH-SERVICE")
public interface AuthClient {
	
	@DeleteMapping("/auth/{username}")
	String deleteUser(@PathVariable String username, @RequestHeader("Authorization") String token);

}
