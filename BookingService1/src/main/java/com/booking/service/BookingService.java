package com.booking.service;

import reactor.core.publisher.Mono;
import java.util.List;

import com.booking.entity.Booking;

public interface BookingService {
	Mono<Booking> createBooking(Long trainId, int passengerName, int seatsBooked);

	Mono<List<Booking>> getBookingsByTrainId(Long trainId);

	Mono<List<Booking>> getAllBookings();
	
	List<Booking> getBookingsByUserId(int userId);

}