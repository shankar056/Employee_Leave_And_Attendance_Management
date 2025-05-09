package com.example.Apigateway.filter;


import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {


	public static final String[] OPEN_API_ENDPOINTS = { "/auth/register", "/auth/new", "/auth/validate", "/eureka" };

	public Predicate<ServerHttpRequest> isSecured = request -> {
		String path = request.getPath().toString();
		for (String endpoint : OPEN_API_ENDPOINTS) {
			if (path.contains(endpoint)) {
				System.out.println("Open endpoint accessed: " + path);
				return false; // Endpoint does not require authorization
			}
		}
		System.out.println("Secured endpoint accessed: " + path);
		return true; // Endpoint requires authorization
	};

	
	

}
