package com.admin.exception;

import feign.FeignException;
import feign.RetryableException;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError; 
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TrainNotFoundException.class)
    public ResponseEntity<String> handleTrainNotFoundException(TrainNotFoundException ex) {
        return new ResponseEntity<>("The requested train was not found. Please verify the Train ID and try again.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TrainServiceException.class)
    public ResponseEntity<String> handleTrainServiceException(TrainServiceException ex) {
        return new ResponseEntity<>("Train Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<String> handleRetryableException(RetryableException ex) {
        return new ResponseEntity<>("Unable to connect to Train Service. Please check your connection and try again.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        return new ResponseEntity<>("Invalid request or server error occurred while communicating with Train Service.", HttpStatus.BAD_GATEWAY);
    }

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<Map<String, String>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getAllErrors().forEach(error -> {
	        if (error instanceof FieldError fieldError) {
	            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
	        } else {
	            errors.put("object", error.getDefaultMessage());
	        }
	    });
	    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
    	System.out.println(ex);
        return new ResponseEntity<>("An unexpected error occurred. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
