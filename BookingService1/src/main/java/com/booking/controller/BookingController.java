package com.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booking.entity.Booking;
import com.booking.service.BookingService;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@PostMapping
	public Mono<ResponseEntity<Object>> createBooking(@RequestParam Long trainId, @RequestParam int userId,
			@RequestParam int seatsBooked) {
		return bookingService.createBooking(trainId, userId, seatsBooked)
				.map(booking -> ResponseEntity.status(HttpStatus.CREATED).body((Object) booking));
	}

	@GetMapping("/train/{trainId}")
	public Mono<ResponseEntity<List<Booking>>> getBookingsByTrainId(@PathVariable Long trainId) {
		return bookingService.getBookingsByTrainId(trainId).map(ResponseEntity::ok);
	}

	@GetMapping
	public Mono<ResponseEntity<List<Booking>>> getAllBookings() {
		return bookingService.getAllBookings().map(ResponseEntity::ok);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable int userId) {
	    List<Booking> bookings = bookingService.getBookingsByUserId(userId);
	    return ResponseEntity.ok(bookings);
	}

}
