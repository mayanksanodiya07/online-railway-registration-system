package com.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booking.entity.Booking;
import com.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestParam Long trainId,
                                                @RequestParam int userId,
                                                @RequestParam int seatsBooked) {
        Booking booking = bookingService.createBooking(trainId, userId, seatsBooked);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<Booking>> getBookingsByTrainId(@PathVariable Long trainId) {
        List<Booking> bookings = bookingService.getBookingsByTrainId(trainId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable int userId, @RequestHeader("Authorization") String token ) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }
}
