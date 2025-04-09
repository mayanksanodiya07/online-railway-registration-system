package com.booking.exception;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import feign.FeignException;
import feign.RetryableException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", ex.getStatusCode().value());
		error.put("error", "Internal Server Error");

		// Check if this is a train not found error
		if (ex.getReason() != null && ex.getReason().contains("Train not found")) {
			error.put("message", "Train not available");
		} else {
			error.put("message", ex.getReason());
		}

		return new ResponseEntity<>(error, ex.getStatusCode());
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<Object> handleFeignException(FeignException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.put("error", "Internal Server Error");

		if (ex.status() == 404) {
			error.put("message", "Train not available ");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			error.put("message", "An unexpected error occurred: No fallback available.");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Exception ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.put("error", "Internal Server Error");

		// Check if this is a train not found issue
		if (ex.getMessage() != null && ex.getMessage().contains("Train not found")) {
			error.put("message", "Train not available ");
		} else {
			error.put("message", "An unexpected error occurred: " + ex.getMessage());
		}

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler({ ConnectException.class, TimeoutException.class, RetryableException.class })
	public ResponseEntity<Object> handleConnectionException(Exception ex) {
	    Map<String, Object> errorBody = new LinkedHashMap<>();
	    errorBody.put("timestamp", LocalDateTime.now());
	    errorBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
	    errorBody.put("error", "Internal Server Error");
	    errorBody.put("message", "Train not available ");
	    return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}