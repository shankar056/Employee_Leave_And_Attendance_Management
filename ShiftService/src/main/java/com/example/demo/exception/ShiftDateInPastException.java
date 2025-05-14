package com.example.demo.exception;

public class ShiftDateInPastException extends RuntimeException {
    public ShiftDateInPastException(String message) {
        super(message);
    }
}