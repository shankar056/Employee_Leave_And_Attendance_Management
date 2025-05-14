package com.example.demo.exception;

public class DuplicateShiftException extends RuntimeException {
    public DuplicateShiftException(String message) {
        super(message);
    }
}