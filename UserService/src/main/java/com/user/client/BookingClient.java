package com.user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url="http://localhost:8084", name="BOOKING-SERVICE")
public interface BookingClient {
	
	@GetMapping("/bookings/user/{userId}")
    List<Object> getBookingsByUserId(@PathVariable Long userId, @RequestHeader("Authorization") String token);

}
