package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.entity.UserInfo;
import com.feignclient.Employeeclient;
import com.repository.UserInfoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserInfoRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Employeeclient employeeClient;

	public String addUser(UserInfo userInfo) {
		String name = userInfo.getName();
		UserInfo existingUser = repository.findByName(name).orElse(null);
		logger.debug("Existing user lookup for name '{}': {}", name, existingUser);

		if (existingUser == null) {
			logger.info("Checking if employee exists with ID: {}", userInfo.getEmployeeId());
			boolean employeeExists = employeeClient.doesEmployeeExist(userInfo.getEmployeeId());
			logger.info("Employee existence result for ID {}: {}", userInfo.getEmployeeId(), employeeExists);

			if (employeeExists) {
				userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
				repository.save(userInfo);
				logger.info("User '{}' registered successfully", name);
				return "Registration Successfully";
			} else {
				logger.warn("Employee ID {} not found in system", userInfo.getEmployeeId());
				return "Employee does not exist.";
			}
		} else {
			logger.warn("Username '{}' is already registered", name);
			return "This UserName is Already Registered.";
		}
	}

	public String getRoles(String username) {
		UserInfo user = repository.findByName(username).orElse(null);
		if (user != null) {
			logger.info("Role fetched for user '{}': {}", username, user.getRole());
			return user.getRole();
		}
		logger.warn("User '{}' not found when fetching role", username);
		return "Not Found";
	}
}