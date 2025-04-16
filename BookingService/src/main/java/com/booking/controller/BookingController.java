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
    public ResponseEntity<Object> createBooking(@RequestBody Booking booking) {
        Booking newBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<Booking>> getBookingsByTrainId(@PathVariable Long trainId) {
        List<Booking> bookings = bookingService.getBookingsByTrainId(trainId);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBookingsByBookingId(@PathVariable Long bookingId) {
        Booking bookings = bookingService.getBookingsByBookingId(bookingId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@RequestHeader("Authorization") String token ) {
        List<Booking> bookings = bookingService.getBookingsByUserId();
        return ResponseEntity.ok(bookings);
    }
    
    @PutMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
    	bookingService.cancelBooking(bookingId);
    	return ResponseEntity.ok("Booking successfully cancled.");
    }
    
//    @DeleteMapping("/{bookingId}")
//    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId) {
//        bookingService.deleteBooking(bookingId);
//        return ResponseEntity.ok("Booking successfully deleted.");
//    }
}
