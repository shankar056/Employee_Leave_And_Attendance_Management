package com.example.demo.exception;

public class ClockOutException extends RuntimeException {
    public ClockOutException (String message) {
        super(message);
    }
}