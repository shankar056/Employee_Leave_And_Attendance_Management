package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.feignclient.LeaveClient;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImp.class);

	private EmployeeRepository employeeRepository;
	private LeaveClient leaveServiceClient;

	public String saveEmployee(Employee employee) {
		logger.info("Attempting to save employee with email: {}", employee.getEmail());

		// Check for duplicate email
		if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
			logger.error("Conflict: An employee with email {} already exists.", employee.getEmail());
			throw new ConflictException("An employee with this email already exists.");
		}

		// Additional custom validation (optional)
		if (employee.getContactno() != null && !employee.getContactno().matches("\\d{10}")) {
			logger.error("Validation failed: Contact number {} is not a 10-digit number.", employee.getContactno());
			throw new ValidationException("Contact number must be a 10-digit number.");
		}

		Employee savedEmployee = employeeRepository.save(employee);
		leaveServiceClient.initializeLeaveBalance(savedEmployee.getId());
		logger.info("Employee saved successfully with id: {}", savedEmployee.getId());
		return "Employee Saved Successfully";
	}

	public List<Employee> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		logger.info("Retrieved {} employees from the database.", employees.size());
		return employees;
	}

	public Optional<Employee> getEmployeeById(Integer id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent()) {
			logger.info("Employee found with id: {}", id);
		} else {
			logger.warn("Employee not found with id: {}", id);
		}
		return employee;
	}

	public String deleteEmployee(Integer id) {
		if (!employeeRepository.existsById(id)) {
			logger.error("Resource not found: Employee with id {} does not exist.", id);
			throw new ResourceNotFoundException("Employee not found with id " + id);
		}
		employeeRepository.deleteById(id);
		logger.info("Employee deleted successfully with id: {}", id);
		return "Employee Deleted Successfully";
	}

	@Override
	public Optional<Employee> getEmployeeByEmail(String email) {
		Optional<Employee> employee = employeeRepository.findByEmail(email);
		if (employee.isPresent()) {
			logger.info("Employee found with email: {}", email);
		} else {
			logger.warn("Employee not found with email: {}", email);
		}
		return employee;
	}

	public Employee updateEmployee(Integer id, Employee employeeDetails) {
		logger.info("Attempting to update employee with id: {}", id);

		Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
			logger.error("Resource not found: Employee with id {} does not exist.", id);
			return new ResourceNotFoundException("Employee not found with id " + id);
		});

		if (!employee.getEmail().equals(employeeDetails.getEmail())
				&& employeeRepository.findByEmail(employeeDetails.getEmail()).isPresent()) {
			logger.error("Conflict: Another employee with email {} already exists.", employeeDetails.getEmail());
			throw new ConflictException("Another employee with this email already exists.");
		}

		employeeDetails.setId(employee.getId());
		employeeRepository.save(employeeDetails);
		logger.info("Employee updated successfully with id: {}", id);
		return employeeDetails;
	}

	public boolean doesEmployeeExist(Integer id) {
		boolean exists = employeeRepository.findById(id).isPresent();
		logger.info("Employee existence check for id {}: {}", id, exists);
		return exists;
	}
}
