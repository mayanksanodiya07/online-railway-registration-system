package com.train.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TrainNotFoundException.class)
    public ResponseEntity<String> handleTrainNotFoundException(TrainNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTrainOperationException.class)
    public ResponseEntity<String> handleInvalidOperationException(InvalidTrainOperationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<String> handleGenericException(Exception ex) {
    	System.out.println(ex);
        return new ResponseEntity<>("Internal server error occurred. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}