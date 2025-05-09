package com.example.demo.controller;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeServiceImp employeeService;

	// http://localhost:1002/employees
	@PostMapping("/save")
	public String createEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}

	// http://localhost:1002/employees/findAllEmployee
	@GetMapping("/findAllEmployee")
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	// http://localhost:1002/employees/findEmployeeById/{id}
	@GetMapping("/findEmployeeById/{id}")
	public Optional<Employee> getEmployeeById(@PathVariable Integer id) {
		return employeeService.getEmployeeById(id);
	}

	// http://localhost:1002/employees/updateEmployee/{id}
	@PutMapping("/updateEmployee/{id}")
	public Employee updateEmployee(@PathVariable Integer id, @RequestBody Employee employeeDetails) throws EmployeeNotFoundException {
		return employeeService.updateEmployee(id, employeeDetails);
	}
	
	@GetMapping("/check/{id}")
	public boolean doesEmployeeExist(@PathVariable Integer id) {
	    return employeeService.getEmployeeById(id).isPresent();
	}
	
	// http://localhost:1002/employees/deleteEmployee/{id}
	@DeleteMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable Integer id) {
		return employeeService.deleteEmployee(id);
	}
}
