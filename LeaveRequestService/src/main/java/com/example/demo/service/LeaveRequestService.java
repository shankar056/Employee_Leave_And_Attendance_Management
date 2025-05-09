package com.example.demo.service;

import com.example.demo.model.LeaveRequest;

import java.util.List;

public interface LeaveRequestService {

	String applyLeave(LeaveRequest request);

	String approveLeave(int requestId);

	String rejectLeave(int requestId);

	List<LeaveRequest> getAllLeaveRequests();

	List<LeaveRequest> getLeaveHistoryByStatus(String status);

	String deleteLeaveRequest(int id);

	List<LeaveRequest> getRequestsByEmployeeId(int employeeId);
}
