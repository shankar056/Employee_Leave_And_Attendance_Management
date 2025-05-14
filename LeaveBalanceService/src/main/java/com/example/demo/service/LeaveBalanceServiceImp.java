package com.example.demo.service;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.exception.InvalidLeaveBalanceException;
import com.example.demo.exception.LeaveInitializationException;
import com.example.demo.exception.LeaveTypeNotFound;
import com.example.demo.model.LeaveBalance;
import com.example.demo.repository.LeaveBalanceRepository;
import com.example.demo.util.LeaveTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImp {

	private final LeaveBalanceRepository repo;

	// Initialize leave balances for an employee
	public void initializeLeaveBalance(int employeeId) {
		try {
			// Check if the leave balances already exist for this employee
			List<LeaveBalance> existingBalances = repo.findByEmployeeId(employeeId);
			if (!existingBalances.isEmpty()) {
				throw new LeaveInitializationException(
						"Leave balances have already been initialized for employee with ID " + employeeId);
			}

			// Initialize leave balances
			LeaveTypes.leaves().forEach((type, count) -> {
				LeaveBalance balance = new LeaveBalance(0, employeeId, type, count);
				repo.save(balance);
			});
		} catch (Exception e) {
			throw new LeaveInitializationException("Error occurred while initializing leave balances for employee with ID " + employeeId);
		}
	}

	// Get leave balance by employee ID and leave type
	public Optional<LeaveBalance> getBalanceByType(int employeeId, String leaveType) throws LeaveTypeNotFound {
		Optional<LeaveBalance> balance = repo.findByEmployeeIdAndLeaveType(employeeId, leaveType);
		if (balance.isEmpty()) {
			throw new LeaveTypeNotFound("Leave type '" + leaveType + "' not found for employee with ID " + employeeId);
		}
		return balance;
	}

	// Update leave balance for an employee
	public String updateLeaveBalance(LeaveBalance updatedBalance) throws LeaveTypeNotFound, InvalidLeaveBalanceException {
		Optional<LeaveBalance> existingBalance = repo.findByEmployeeIdAndLeaveType(updatedBalance.getEmployeeId(),
				updatedBalance.getLeaveType());

		if (existingBalance.isPresent()) {
			LeaveBalance existing = existingBalance.get();

			// Validate the updated balance (ensure it's not negative)
			if (updatedBalance.getBalance() < 0) {
				throw new InvalidLeaveBalanceException("Leave balance cannot be negative.");
			}

			existing.setBalance(updatedBalance.getBalance());
			repo.save(existing);
			return "Leave balance updated successfully for employee ID " + updatedBalance.getEmployeeId();
		} else {
			throw new LeaveTypeNotFound("Leave type '" + updatedBalance.getLeaveType()
					+ "' not found for employee with ID " + updatedBalance.getEmployeeId());
		}
	}

	// Get all leave balances by employee ID
	public List<LeaveBalance> getLeaveBalancesByEmployeeId(int employeeId) throws EmployeeNotFoundException {
		List<LeaveBalance> balances = repo.findByEmployeeId(employeeId);
		if (balances.isEmpty()) {
			throw new EmployeeNotFoundException("No leave balances found for employee with ID " + employeeId);
		}
		return balances;
	}
}