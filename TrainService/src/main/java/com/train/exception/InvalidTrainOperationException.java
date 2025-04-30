package com.train.exception;

public class InvalidTrainOperationException extends RuntimeException {
    public InvalidTrainOperationException(String message) {
        super(message);
    }
}