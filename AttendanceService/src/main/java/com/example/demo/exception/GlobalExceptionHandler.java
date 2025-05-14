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

	private static final String TIMESTAMP = "timestamp";
	private static final String STATUS = "status";
	private static final String ERROR = "error";
	private static final String MESSAGE = "message";
	private static final String PATH = "path";
	private static final String VALIDATION_ERROR = "Validation Error";
	private static final String BAD_REQUEST = "Bad Request";
	private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
	private static final String ATTENDANCE_PATH = "/attendance";
	private static final String CLOCKIN_PATH = "/attendance/clockin";
	private static final String CLOCKOUT_PATH = "/attendance/clockout";

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.BAD_REQUEST.value());
		response.put(ERROR, VALIDATION_ERROR);
		response.put(MESSAGE, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		response.put(PATH, ATTENDANCE_PATH);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.BAD_REQUEST.value());
		response.put(ERROR, VALIDATION_ERROR);
		response.put(MESSAGE, ex.getMessage());
		response.put(PATH, ATTENDANCE_PATH);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ClockInException.class)
	public ResponseEntity<Map<String, Object>> handleClockInException(ClockInException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.BAD_REQUEST.value());
		response.put(ERROR, BAD_REQUEST);
		response.put(MESSAGE, ex.getMessage());
		response.put(PATH, CLOCKIN_PATH);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ClockOutException.class)
	public ResponseEntity<Map<String, Object>> handleClockOutException(ClockOutException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.BAD_REQUEST.value());
		response.put(ERROR, BAD_REQUEST);
		response.put(MESSAGE, ex.getMessage());
		response.put(PATH, CLOCKOUT_PATH);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AttendanceException.class)
	public ResponseEntity<Map<String, Object>> handleAttendanceException(AttendanceException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.BAD_REQUEST.value());
		response.put(ERROR, BAD_REQUEST);
		response.put(MESSAGE, ex.getMessage());
		response.put(PATH, ATTENDANCE_PATH);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.put(ERROR, INTERNAL_SERVER_ERROR);
		response.put(MESSAGE, ex.getMessage());
		response.put(PATH, ATTENDANCE_PATH);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
