package com.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	Optional<Booking> findById(Long id);

	List<Booking> findByTrainId(Long trainId);
	
	List<Booking> findByAuthUserId(Long userId);

}
