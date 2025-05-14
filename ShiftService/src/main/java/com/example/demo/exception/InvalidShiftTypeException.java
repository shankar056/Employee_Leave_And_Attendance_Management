package com.example.demo.exception;

public class InvalidShiftTypeException extends RuntimeException {
    public InvalidShiftTypeException(String message) {
        super(message);
    }
}