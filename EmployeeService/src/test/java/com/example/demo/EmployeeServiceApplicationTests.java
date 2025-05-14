package com.example.demo;

import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.feignclient.LeaveClient;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeServiceImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceApplicationTests.class);

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveClient leaveClient;

    @InjectMocks
    private EmployeeServiceImp employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setEmail("john@example.com");
        employee.setContactno("1234567890");
        logger.info("Setup complete for EmployeeServiceApplicationTests");
    }

    @Test
    void testSaveEmployee_Success() {
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.save(employee)).thenReturn(employee);

        String result = employeeService.saveEmployee(employee);

        assertEquals("Employee Saved Successfully", result);
        verify(leaveClient).initializeLeaveBalance(employee.getId());
        logger.info("Employee saved successfully with email: {}", employee.getEmail());
    }

    @Test
    void testSaveEmployee_DuplicateEmail() {
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        assertThrows(ConflictException.class, () -> employeeService.saveEmployee(employee));
        logger.error("Conflict: An employee with email {} already exists.", employee.getEmail());
    }

    @Test
    void testSaveEmployee_InvalidContact() {
        employee.setContactno("123");
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> employeeService.saveEmployee(employee));
        logger.error("Validation failed: Contact number {} is not a 10-digit number.", employee.getContactno());
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> list = List.of(employee);
        when(employeeRepository.findAll()).thenReturn(list);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        logger.info("Retrieved {} employees from the database.", result.size());
    }

    @Test
    void testGetEmployeeById_Found() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        logger.info("Employee found with id: {}", 1);
    }

    @Test
    void testDeleteEmployee_Success() {
        when(employeeRepository.existsById(1)).thenReturn(true);

        String result = employeeService.deleteEmployee(1);

        assertEquals("Employee Deleted Successfully", result);
        verify(employeeRepository).deleteById(1);
        logger.info("Employee deleted successfully with id: {}", 1);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.existsById(1)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(1));
        logger.error("Resource not found: Employee with id {} does not exist.", 1);
    }

    @Test
    void testUpdateEmployee_Success() {
        Employee updated = new Employee();
        updated.setEmail("new@example.com");
        updated.setName("Updated Name");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(updated);

        Employee result = employeeService.updateEmployee(1, updated);

        assertEquals("Updated Name", result.getName());
        assertEquals(1, result.getId());
        logger.info("Employee updated successfully with id: {}", 1);
    }

    @Test
    void testUpdateEmployee_EmailConflict() {
        Employee updated = new Employee();
        updated.setEmail("other@example.com");

        Employee another = new Employee();
        another.setEmail("other@example.com");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail("other@example.com")).thenReturn(Optional.of(another));

        assertThrows(ConflictException.class, () -> employeeService.updateEmployee(1, updated));
        logger.error("Conflict: Another employee with email {} already exists.", updated.getEmail());
    }

    @Test
    void testDoesEmployeeExist() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        assertTrue(employeeService.doesEmployeeExist(1));
        logger.info("Employee existence check for id {}: {}", 1, true);

        when(employeeRepository.findById(2)).thenReturn(Optional.empty());
        assertFalse(employeeService.doesEmployeeExist(2));
        logger.info("Employee existence check for id {}: {}", 2, false);
    }
}
