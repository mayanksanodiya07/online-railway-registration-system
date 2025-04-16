package com.booking.service;

import java.util.List;
import com.booking.entity.Booking;

public interface BookingService {

    Booking createBooking(Booking booking);

    List<Booking> getBookingsByTrainId(Long trainId);

    Booking getBookingsByBookingId(Long bookingId);

    List<Booking> getBookingsByUserId();
    
    List<Booking> getAllBookings();

	void cancelBooking(Long bookingId);

//	void deleteBooking(Long bookingId);
}
