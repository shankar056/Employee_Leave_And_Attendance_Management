package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.example.demo.dto.LeaveBalanceDTO;
import com.example.demo.feignclient.BalanceLeave;
import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.service.LeaveRequestServiceImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LeaveRequestServiceApplicationTests {

	@Mock
	private LeaveRequestRepository requestRepo;

	@Mock
	private BalanceLeave balanceLeave;

	@InjectMocks
	private LeaveRequestServiceImp leaveRequestServiceImp;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testApplyLeave_Success() {
		LeaveRequest request = new LeaveRequest();
		request.setEmployeeId(1);
		request.setLeaveType("Sick");
		request.setStartDate(new Date(System.currentTimeMillis()));
		request.setEndDate(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));

		LeaveBalanceDTO balanceDTO = new LeaveBalanceDTO();
		balanceDTO.setBalance(5);

		when(balanceLeave.getBalanceByType(1, "Sick")).thenReturn(balanceDTO);

		String response = leaveRequestServiceImp.applyLeave(request);

		assertEquals("Leave applied.", response);
		verify(requestRepo, times(1)).save(any(LeaveRequest.class));
	}

	@Test
	void testApplyLeave_InsufficientBalance(){
		LeaveRequest request = new LeaveRequest();
		request.setEmployeeId(1);
		request.setLeaveType("Sick");
		request.setStartDate(new Date());
		request.setEndDate(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5)));

		LeaveBalanceDTO balanceDTO = new LeaveBalanceDTO();
		balanceDTO.setBalance(2);

		when(balanceLeave.getBalanceByType(1, "Sick")).thenReturn(balanceDTO);

		String response = leaveRequestServiceImp.applyLeave(request);

		assertEquals("Insufficient leave balance.", response);
	}

	@Test
	void testApproveLeave_Success() {
		LeaveRequest request = new LeaveRequest();
		request.setLeaveId(101);
		request.setStatus("Pending");
		request.setEmployeeId(1);
		request.setLeaveType("Casual");
		request.setStartDate(new Date());
		request.setEndDate(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));

		LeaveBalanceDTO balanceDTO = new LeaveBalanceDTO();
		balanceDTO.setBalance(10);

		when(requestRepo.findById(101)).thenReturn(Optional.of(request));
		when(balanceLeave.getBalanceByType(1, "Casual")).thenReturn(balanceDTO);

		String result = leaveRequestServiceImp.approveLeave(101);

		assertEquals("Approved", result);
		verify(balanceLeave, times(1)).updateLeaveBalance(any(LeaveBalanceDTO.class));
	}

	@Test
	void testRejectLeave() {
		LeaveRequest request = new LeaveRequest();
		request.setLeaveId(102);
		request.setStatus("Pending");

		when(requestRepo.findById(102)).thenReturn(Optional.of(request));

		String result = leaveRequestServiceImp.rejectLeave(102);

		assertEquals("Rejected", result);
		verify(requestRepo, times(1)).save(any(LeaveRequest.class));
	}

	@Test
	void testGetAllRequests() {
		List<LeaveRequest> requests = Arrays.asList(new LeaveRequest(), new LeaveRequest());
		when(requestRepo.findAll()).thenReturn(requests);

		List<LeaveRequest> result = leaveRequestServiceImp.getAllRequests();
		assertEquals(2, result.size());
	}

	@Test
	void testDeleteLeaveRequest() {
		String response = leaveRequestServiceImp.deleteLeaveRequest(5);
		assertEquals("Leave Deleted", response);
		verify(requestRepo, times(1)).deleteById(5);
	}
}