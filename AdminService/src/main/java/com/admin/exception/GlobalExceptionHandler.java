package com.admin.exception;

import feign.FeignException;
import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🚂 Train not found
    @ExceptionHandler(TrainNotFoundException.class)
    public ResponseEntity<String> handleTrainNotFoundException(TrainNotFoundException ex) {
        return new ResponseEntity<>("The requested train was not found. Please verify the Train ID and try again.", HttpStatus.NOT_FOUND);
    }

    // 🚂 General Train service error
    @ExceptionHandler(TrainServiceException.class)
    public ResponseEntity<String> handleTrainServiceException(TrainServiceException ex) {
        return new ResponseEntity<>("Train Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    // 🔌 Unable to connect to TrainService (service down or unreachable)
    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<String> handleRetryableException(RetryableException ex) {
        return new ResponseEntity<>("Unable to connect to Train Service. Please try again after some time.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    // 🌐 Other Feign-related errors
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        if (ex.status() >= 500) {
            return new ResponseEntity<>("Train Service is experiencing issues. Please try again shortly.", HttpStatus.BAD_GATEWAY);
        } else {
            return new ResponseEntity<>("An unexpected error occurred while communicating with Train Service.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ⚠️ Catch-all fallback for any unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
