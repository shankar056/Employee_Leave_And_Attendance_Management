package com.example.demo;

import com.example.demo.exception.LeaveInitializationException;
import com.example.demo.exception.LeaveTypeNotFound;
import com.example.demo.model.LeaveBalance;
import com.example.demo.repository.LeaveBalanceRepository;
import com.example.demo.service.LeaveBalanceServiceImp;
import com.example.demo.util.LeaveTypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaveBalanceServiceApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(LeaveBalanceServiceApplicationTests.class);

	@Mock
	private LeaveBalanceRepository leaveBalanceRepository;

	@InjectMocks
	private LeaveBalanceServiceImp leaveBalanceService;

	private LeaveBalance leaveBalance;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		leaveBalance = new LeaveBalance();
		leaveBalance.setId(1);
		leaveBalance.setEmployeeId(1001);
		leaveBalance.setLeaveType("Sick");
		leaveBalance.setBalance(5);

		logger.info("Setup complete with LeaveBalance: {}", leaveBalance);
	}

	@Test
	void testInitializeLeaveBalance() throws LeaveInitializationException {
		Map<String, Integer> mockLeaveTypes = new HashMap<>();
		mockLeaveTypes.put("Sick", 5);
		mockLeaveTypes.put("Casual", 3);

		try (MockedStatic<LeaveTypes> mocked = mockStatic(LeaveTypes.class)) {
			mocked.when(LeaveTypes::leaves).thenReturn(mockLeaveTypes);

			leaveBalanceService.initializeLeaveBalance(1001);

			verify(leaveBalanceRepository, times(2)).save(any(LeaveBalance.class));
			logger.info("Leave balance initialized for employeeId: 1001");
		}
	}

	@Test
	void testGetBalanceByType() {
		when(leaveBalanceRepository.findByEmployeeIdAndLeaveType(1001, "Sick")).thenReturn(Optional.of(leaveBalance));

		Optional<LeaveBalance> result = leaveBalanceService.getBalanceByType(1001, "Sick");

		assertTrue(result.isPresent());
		assertEquals("Sick", result.get().getLeaveType());
		logger.info("Retrieved leave balance for employeeId: 1001, leaveType: Sick");
	}

	@Test
	void testUpdateLeaveBalance_Success() throws LeaveTypeNotFound {
		LeaveBalance updated = new LeaveBalance(1, 1001, "Sick", 10);

		when(leaveBalanceRepository.findByEmployeeIdAndLeaveType(1001, "Sick")).thenReturn(Optional.of(leaveBalance));

		String result = leaveBalanceService.updateLeaveBalance(updated);

		assertEquals("Leave balance updated successfully.", result);
		verify(leaveBalanceRepository).save(any(LeaveBalance.class));
		logger.info("Leave balance updated for employeeId: 1001, leaveType: Sick");
	}

	@Test
	void testUpdateLeaveBalance_ThrowsLeaveTypeNotFound() {
		LeaveBalance updated = new LeaveBalance(1, 1001, "Vacation", 5);

		when(leaveBalanceRepository.findByEmployeeIdAndLeaveType(1001, "Vacation")).thenReturn(Optional.empty());

		assertThrows(LeaveTypeNotFound.class, () -> leaveBalanceService.updateLeaveBalance(updated));
		logger.error("LeaveTypeNotFound exception thrown for employeeId: 1001, leaveType: Vacation");
	}

	@Test
	void testGetLeaveBalancesByEmployeeId() {
		List<LeaveBalance> balances = List.of(leaveBalance);

		when(leaveBalanceRepository.findByEmployeeId(1001)).thenReturn(balances);

		List<LeaveBalance> result = leaveBalanceService.getLeaveBalancesByEmployeeId(1001);

		assertEquals(1, result.size());
		assertEquals("Sick", result.get(0).getLeaveType());
		logger.info("Retrieved leave balances for employeeId: 1001");
	}
}
