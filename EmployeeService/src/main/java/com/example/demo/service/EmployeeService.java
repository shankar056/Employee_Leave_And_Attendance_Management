package com.example.demo.service;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
	String saveEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Optional<Employee> getEmployeeById(Integer id);

	String deleteEmployee(Integer id);

	Employee updateEmployee(Integer id, Employee employeeDetails) throws EmployeeNotFoundException;

	boolean doesEmployeeExist(Integer id);
}
