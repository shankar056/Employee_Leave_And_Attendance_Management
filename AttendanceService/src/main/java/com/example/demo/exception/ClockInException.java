package com.example.demo.exception;

public class ClockInException extends RuntimeException {
    public  ClockInException (String message) {
        super(message);
    }
}