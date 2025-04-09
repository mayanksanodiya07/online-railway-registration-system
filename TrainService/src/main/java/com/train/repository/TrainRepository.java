package com.train.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.train.entity.Train;

import jakarta.transaction.Transactional;

public interface TrainRepository extends JpaRepository<Train, Long> {
	List<Train> findBySourceAndDestination(String source, String destination);

	@Transactional
	@Modifying
	@Query("UPDATE Train t SET t.bookedSeats = ?2, t.availableSeats = ?3 WHERE t.id = ?1")
	void updateBookedSeats(Long trainId, int bookedSeats, int availableSeats);
}
