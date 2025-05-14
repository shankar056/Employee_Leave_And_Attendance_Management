package com.example.demo.service;

import java.util.List;
import com.example.demo.model.LeaveRequest;

public interface LeaveRequestService {

	String applyLeave(LeaveRequest request);

	String approveLeave(int requestId);

	String rejectLeave(int requestId);

	List<LeaveRequest> getAllLeaveRequests();

	List<LeaveRequest> getLeaveHistoryByStatus(String status);

	String deleteLeaveRequest(int id);

	List<LeaveRequest> getRequestsByEmployeeId(int employeeId);
}
