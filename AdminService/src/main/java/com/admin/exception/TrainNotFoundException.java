package com.admin.exception;

public class TrainNotFoundException extends RuntimeException {
    public TrainNotFoundException(String message) {
        super(message);
    }
}
