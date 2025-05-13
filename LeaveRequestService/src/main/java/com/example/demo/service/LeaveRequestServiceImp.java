package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.feignclient.BalanceLeave;
import com.example.demo.dto.LeaveBalanceDTO;
import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.LeaveRequestRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LeaveRequestServiceImp {

	LeaveRequestRepository requestRepo;

	BalanceLeave balanceleave;

	public String applyLeave(LeaveRequest request) {
		LeaveBalanceDTO balance = balanceleave.getBalanceByType(request.getEmployeeId(), request.getLeaveType());

		if (balance == null || balance.getBalance() == null) {
			return "Leave balance not found.";
		}

		long diff = request.getEndDate().getTime() - request.getStartDate().getTime();
		int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

		if (balance.getBalance() < days) {
			return "Insufficient leave balance.";
		}

		request.setStatus("Pending");
		requestRepo.save(request);
		return "Leave applied.";
	}

	public String approveLeave(int requestId) {
		Optional<LeaveRequest> opt = requestRepo.findById(requestId);
		if (opt.isEmpty())
			return "Request not found";

		LeaveRequest req = opt.get();
		if (!"Pending".equals(req.getStatus()))
			return "Already processed";

		LeaveBalanceDTO balance = balanceleave.getBalanceByType(req.getEmployeeId(), req.getLeaveType());
		int days = (int) TimeUnit.DAYS.convert(req.getEndDate().getTime() - req.getStartDate().getTime(),
				TimeUnit.MILLISECONDS) + 1;

		if (balance.getBalance() < days)
			return "Insufficient balance";

		balance.setBalance(balance.getBalance() - days);
		balanceleave.updateLeaveBalance(balance);

		req.setStatus("Approved");
		requestRepo.save(req);
		return "Approved";
	}

	public String rejectLeave(int requestId) {
		Optional<LeaveRequest> opt = requestRepo.findById(requestId);
		if (opt.isEmpty())
			return "Request not found";

		LeaveRequest req = opt.get();
		if (!"Pending".equals(req.getStatus()))
			return "Already processed";

		req.setStatus("Rejected");
		requestRepo.save(req);
		return "Rejected";
	}

	public List<LeaveRequest> getAllRequests() {
		return requestRepo.findAll();
	}

	public List<LeaveRequest> getRequestsByStatus(String status) {
		return requestRepo.findByStatus(status);
	}

	public String deleteLeaveRequest(int id) {
		requestRepo.deleteById(id);
		return "Leave Deleted";
	}
	
	public List<LeaveRequest> getRequestsByEmployeeId(int employeeId) {
        return requestRepo.findByEmployeeId(employeeId);
    }

}