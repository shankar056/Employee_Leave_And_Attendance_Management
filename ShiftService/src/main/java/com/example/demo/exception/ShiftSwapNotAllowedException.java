package com.example.demo.exception;

public class ShiftSwapNotAllowedException extends RuntimeException {
    public ShiftSwapNotAllowedException(String message) {
        super(message);
    }
}