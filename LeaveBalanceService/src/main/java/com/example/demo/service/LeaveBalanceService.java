package com.example.demo.service;

import com.example.demo.exception.LeaveTypeNotFound;
import com.example.demo.model.LeaveBalance;

import java.util.List;
import java.util.Optional;


public interface LeaveBalanceService {

	void initializeLeaveBalanceForEmployee(int employeeId);

	List<LeaveBalance> getLeaveBalancesByEmployeeId(int employeeId);

	Optional<LeaveBalance> findLeaveBalance(int employeeId, String leaveType);

	void updateLeaveBalance(LeaveBalance balance)throws LeaveTypeNotFound;
}
