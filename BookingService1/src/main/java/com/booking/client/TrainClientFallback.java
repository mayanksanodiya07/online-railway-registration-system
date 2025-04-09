package com.booking.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainClientFallback implements TrainClient {
	@Override
	public ResponseEntity<Map<String, Object>> getTrainById(Long id) {
		// Instead of throwing an exception, return a ResponseEntity with an error
		// message
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("error", "Train not available ");
		errorResponse.put("status", 404);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<String> bookSeats(Long id, int seats) {
		return new ResponseEntity<>("Failed to book seats - Train not available", HttpStatus.SERVICE_UNAVAILABLE);
	}
}