package com.booking.service;

import java.util.List;
import com.booking.entity.Booking;

public interface BookingService {

    Booking createBooking(Long trainId, int userId, int seatsBooked);

    List<Booking> getBookingsByTrainId(Long trainId);

    List<Booking> getAllBookings();

    List<Booking> getBookingsByUserId(int userId);
}
