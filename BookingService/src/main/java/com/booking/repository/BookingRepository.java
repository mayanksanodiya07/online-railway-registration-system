package com.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.entity.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByTrainId(Long trainId);
	
	List<Booking> findByUserId(int userId);

}
