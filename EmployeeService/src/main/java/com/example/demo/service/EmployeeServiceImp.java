package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.feignclient.LeaveClient;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImp implements EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private LeaveClient leaveServiceClient;

	public String saveEmployee(Employee employee) {
		Employee savedEmployee = employeeRepository.save(employee);
		leaveServiceClient.initializeLeaveBalance(savedEmployee.getId());
		return "Employee saved Successfully";
	}

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Optional<Employee> getEmployeeById(Integer id) {
		return employeeRepository.findById(id);
	}

	public String deleteEmployee(Integer id) {
		employeeRepository.deleteById(id);
		return "Employee Deleted Successfully";
	}

	public Employee updateEmployee(Integer id, Employee employeeDetails) throws EmployeeNotFoundException{
		Employee existingEmployee = employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee with id " + id + " not found in the database"));

		employeeDetails.setId(existingEmployee.getId());

		employeeRepository.save(employeeDetails);
		return employeeDetails;
	}
	
	public boolean doesEmployeeExist(Integer id) {
  	  return employeeRepository.findById(id).isPresent();
  	  }

}
