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

    private static final String SHIFT_PATH = "/shifts";

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
        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, "Validation Error", errorMessage, SHIFT_PATH), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShiftNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleShiftNotFound(ShiftNotFoundException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.NOT_FOUND, "Shift Not Found", ex.getMessage(), SHIFT_PATH), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateShiftException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateShift(DuplicateShiftException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.CONFLICT, "Duplicate Shift", ex.getMessage(), SHIFT_PATH), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidShiftTypeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidShiftType(InvalidShiftTypeException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, "Invalid Shift Type", ex.getMessage(), SHIFT_PATH), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShiftSwapNotAllowedException.class)
    public ResponseEntity<Map<String, Object>> handleSwapNotAllowed(ShiftSwapNotAllowedException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.FORBIDDEN, "Swap Not Allowed", ex.getMessage(), SHIFT_PATH), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ShiftAlreadySwappedException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadySwapped(ShiftAlreadySwappedException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, "Already Swapped", ex.getMessage(), SHIFT_PATH), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShiftDateInPastException.class)
    public ResponseEntity<Map<String, Object>> handleDateInPast(ShiftDateInPastException ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, "Invalid Date", ex.getMessage(), SHIFT_PATH), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return new ResponseEntity<>(createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), SHIFT_PATH), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}