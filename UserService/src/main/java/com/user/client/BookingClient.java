package com.user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.user.dto.BookingDto;

@FeignClient(url="http://localhost:8084", name="BOOKING-SERVICE")
public interface BookingClient {
	
	@GetMapping("/bookings/user")
    List<BookingDto> getBookingsByUserId(@RequestHeader("Authorization") String token);

}
