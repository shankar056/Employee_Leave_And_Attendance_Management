package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private static final String UNAUTHORIZED = "Unauthorized";
	private static final String FORBIDDEN = "Forbidden";
	private static final String BAD_REQUEST = "Bad Request";
	private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
	private static final String AUTH_PATH = "/auth";

	@ExceptionHandler(TokenValidationException.class)
	public ResponseEntity<Map<String, Object>> handleTokenValidationException(TokenValidationException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.UNAUTHORIZED.value());
		response.put(ERROR, UNAUTHORIZED);
		response.put(MESSAGE, "The provided token is invalid. Please check your token and try again.");
		response.put(PATH, AUTH_PATH);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.FORBIDDEN.value());
		response.put(ERROR, FORBIDDEN);
		response.put(MESSAGE, "You do not have permission to access this resource.");
		response.put(PATH, AUTH_PATH);
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MissingAuthorizationHeaderException.class)
	public ResponseEntity<Map<String, Object>> handleMissingAuthorizationHeaderException(
			MissingAuthorizationHeaderException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.BAD_REQUEST.value());
		response.put(ERROR, BAD_REQUEST);
		response.put(MESSAGE, "Authorization header is missing. Please provide a valid authorization header.");
		response.put(PATH, AUTH_PATH);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<Map<String, Object>> handleAuthException(AuthException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.BAD_REQUEST.value());
		response.put(ERROR, BAD_REQUEST);
		response.put(MESSAGE, "An authentication error occurred. Please check your credentials and try again.");
		response.put(PATH, AUTH_PATH);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(TIMESTAMP, LocalDateTime.now());
		response.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.put(ERROR, INTERNAL_SERVER_ERROR);
		response.put(MESSAGE, "An unexpected error occurred. Please try again later.");
		response.put(PATH, AUTH_PATH);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
