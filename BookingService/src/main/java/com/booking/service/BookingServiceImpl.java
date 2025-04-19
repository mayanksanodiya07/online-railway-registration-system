package com.booking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.booking.client.TrainClient;
import com.booking.entity.Booking;
import com.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TrainClient trainClient;

    @Override
    public Booking createBooking(Booking booking) {
    	
    	Long trainId = booking.getTrainId();
    	
    	Long userId = getUserId(); 
        
    	int seatsBooked =booking.getSeatsBooked();
    	
        logger.info("Creating booking for trainId: {}, userId: {}, seats: {}", trainId, userId, seatsBooked);

        if (trainId == null || trainId <= 0) {
            logger.error("Invalid trainId: {}", trainId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid train ID");
        }

        if (userId <= 0) {
            logger.error("Invalid userId: {}", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        }

        if (seatsBooked <= 0) {
            logger.error("Invalid seatsBooked: {}", seatsBooked);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of seats must be positive");
        }

        try {
            ResponseEntity<Map<String, Object>> trainResponse = trainClient.getTrainById(trainId);
            logger.info("Train service response status: {}", trainResponse.getStatusCode());

            if (trainResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.error("Train not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not available with this ID");
            }

            if (!trainResponse.getStatusCode().is2xxSuccessful() || trainResponse.getBody() == null
                    || !trainResponse.getBody().containsKey("availableSeats")) {
                logger.error("Train service unavailable or invalid response");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Train not available");
            }

            Map<String, Object> train = trainResponse.getBody();
            int availableSeats = ((Number) train.get("availableSeats")).intValue();
            logger.info("Available seats: {}", availableSeats);

            if (seatsBooked > availableSeats) {
                logger.error("Not enough seats. Requested: {}, Available: {}", seatsBooked, availableSeats);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not enough seats available. Requested: " + seatsBooked + ", Available: " + availableSeats);
            }

            ResponseEntity<String> response = trainClient.bookSeats(trainId, seatsBooked);
            logger.info("Seat booking response status: {}", response.getStatusCode());

            if (response.getStatusCode().isError()) {
                logger.error("Failed to book seats: {}", response.getBody());
                throw new ResponseStatusException(response.getStatusCode(),
                        "Failed to book seats: " + response.getBody());
            }

            booking.setAuthUserId(userId);
            booking.setBookingTime(LocalDateTime.now());
            booking.setStatus("CONFIRMED");
            
            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Booking saved successfully: {}", savedBooking);
            return savedBooking;

        } catch (feign.FeignException fe) {
            logger.error("Feign client exception: {}", fe.getMessage());
            if (fe.status() == 404) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Train not available");
            }
            throw fe;
        } catch (Exception e) {
            logger.error("Unexpected error during booking creation", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Booking> getBookingsByTrainId(Long trainId) {
        try {
            return bookingRepository.findByTrainId(trainId);
        } catch (Exception e) {
            logger.error("Failed to get bookings by train id: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to get bookings by train id: " + e.getMessage());
        }
    }

    @Override
    public Booking getBookingsByBookingId(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Booking not found with ID: " + bookingId
            ));

        Long userIdFromToken = getUserId();

        if (!booking.getAuthUserId().equals(userIdFromToken)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found for you.");
        }

        return booking;
    }
    
//    @Override
//    public void deleteBooking(Long bookingId) {
//        // Find booking by ID
//        Booking booking = bookingRepository.findById(bookingId)
//            .orElseThrow(() -> new ResponseStatusException(
//                HttpStatus.NOT_FOUND, 
//                "Booking not found with ID: " + bookingId
//            ));
//
//        Long userIdFromToken = getUserId();
//
//        // Check if the booking belongs to the authenticated user
//        if (!booking.getAuthUserId().equals(userIdFromToken)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found for you.");
//        }
//
//        // Proceed to delete the booking
//        bookingRepository.delete(booking);
//        logger.info("Booking with ID {} has been deleted.", bookingId);
//    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Booking not found with ID: " + bookingId
            ));

        Long userIdFromToken = getUserId();

        if (!booking.getAuthUserId().equals(userIdFromToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to cancel this booking.");
        }

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking is already cancelled.");
        }

        // Release seats back to train
        try {
            ResponseEntity<String> response = trainClient.releaseSeats(booking.getTrainId(), booking.getSeatsBooked());

            if (response.getStatusCode().isError()) {
                throw new ResponseStatusException(response.getStatusCode(), "Failed to release seats: " + response.getBody());
            }
 
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);

        } catch (feign.FeignException fe) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while releasing seats: " + fe.getMessage(), fe);
        }
    }

    @Override
    public List<Booking> getBookingsByUserId() {
    	Long userId = getUserId(); 
        try {
            return bookingRepository.findByAuthUserId(userId);
        } catch (Exception e) {
            logger.error("Failed to get bookings for user id {}: {}", userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to get bookings for user id: " + e.getMessage());
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        try {
            return bookingRepository.findAll();
        } catch (Exception e) {
            logger.error("Failed to get all bookings: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to get all bookings: " + e.getMessage());
        }
    }

    
    private Long getUserId() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	return (Long) authentication.getPrincipal(); 
        
    }

}
