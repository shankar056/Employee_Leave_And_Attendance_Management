package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LEAVE_BALANCE_PATH = "/leavebalance";

    private Map<String, Object> createResponse(HttpStatus status, String error, String message, String path) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        response.put("path", path);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, "Validation Error", errorMessage, LEAVE_BALANCE_PATH), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LeaveInitializationException.class)
    public ResponseEntity<Map<String, Object>> handleLeaveInitializationException(LeaveInitializationException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, "Leave Initialization Error", ex.getMessage(), LEAVE_BALANCE_PATH), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LeaveTypeNotFound.class)
    public ResponseEntity<Map<String, Object>> handleLeaveTypeNotFound(LeaveTypeNotFound ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.NOT_FOUND, "Leave Type Not Found", ex.getMessage(), LEAVE_BALANCE_PATH), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.NOT_FOUND, "Employee Not Found", ex.getMessage(), LEAVE_BALANCE_PATH), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidLeaveBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidLeaveBalanceException(InvalidLeaveBalanceException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, "Invalid Leave Balance", ex.getMessage(), LEAVE_BALANCE_PATH), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), LEAVE_BALANCE_PATH), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), LEAVE_BALANCE_PATH), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
