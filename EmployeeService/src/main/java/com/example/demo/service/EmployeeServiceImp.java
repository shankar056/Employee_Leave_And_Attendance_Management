package com.example.demo.service;

import java.util.List;
import java.util.Optional;

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

	private EmployeeRepository employeeRepository;
	private LeaveClient leaveServiceClient;

	public String saveEmployee(Employee employee) {
        // Check for duplicate email
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new ConflictException("An employee with this email already exists.");
        }
 
        // Additional custom validation (optional)
        if (employee.getContactno() != null && !employee.getContactno().matches("\\d{10}")) {
            throw new ValidationException("Contact number must be a 10-digit number.");
        }
 
        Employee savedEmployee = employeeRepository.save(employee);
        leaveServiceClient.initializeLeaveBalance(savedEmployee.getId());
        return "Employee Saved Successfully";
    }
 

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Optional<Employee> getEmployeeById(Integer id) {
		return employeeRepository.findById(id);
	}

	public String deleteEmployee(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id " + id);
        }
        employeeRepository.deleteById(id);
		return "Employee Deleted Successfully";
    }

	@Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
	
    public Employee updateEmployee(Integer id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
 
        if (!employee.getEmail().equals(employeeDetails.getEmail()) &&
                employeeRepository.findByEmail(employeeDetails.getEmail()).isPresent()) {
            throw new ConflictException("Another employee with this email already exists.");
        }

		employeeDetails.setId(employee.getId());

		employeeRepository.save(employeeDetails);
		return employeeDetails;
	}
	
	public boolean doesEmployeeExist(Integer id) {
  	  return employeeRepository.findById(id).isPresent();
  	  }

}
