package com.example.demo;

import com.example.demo.exception.ShiftNotFoundException;
import com.example.demo.model.Shift;
import com.example.demo.repository.ShiftRepository;
import com.example.demo.service.ShiftServiceImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftServiceApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(ShiftServiceApplicationTests.class);

	@Mock
	private ShiftRepository repository;

	@InjectMocks
	private ShiftServiceImp service;

	private Shift shift;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		shift = new Shift();
		shift.setId(1);
		shift.setEmployeeId(1001);
		shift.setShiftType("Day");
		shift.setSwapRequested(false);
		logger.info("Setup complete for ShiftServiceImpTests");
	}

	@Test
	void testFindAll() {
		List<Shift> shifts = List.of(shift);
		when(repository.findAll()).thenReturn(shifts);

		List<Shift> result = service.findAll();

		assertEquals(1, result.size());
		assertEquals("Day", result.get(0).getShiftType());
		logger.info("Retrieved {} shifts from the database.", result.size());
	}

	@Test
	void testFindById_Success() throws ShiftNotFoundException {
		when(repository.findById(1)).thenReturn(Optional.of(shift));

		Shift result = service.findById(1);

		assertNotNull(result);
		assertEquals(1001, result.getEmployeeId());
		logger.info("Shift found with ID: {}", 1);
	}

	@Test
	void testFindById_NotFound() {
		when(repository.findById(1)).thenReturn(Optional.empty());

		assertThrows(ShiftNotFoundException.class, () -> service.findById(1));
		logger.warn("Shift not found with ID: {}", 1);
	}

	@Test
	void testSave() {
		service.save(shift);

		verify(repository).save(shift);
		logger.info("Shift saved for employeeId: {}", shift.getEmployeeId());
	}

	@Test
	void testDeleteById() {
		String result = service.deleteById(1);

		assertEquals("Shift Deleted", result);
		verify(repository).deleteById(1);
		logger.info("Shift deleted with ID: {}", 1);
	}

	@Test
	void testRequestSwap_Success() throws ShiftNotFoundException {
		when(repository.findByEmployeeId(1001)).thenReturn(Optional.of(shift));

		String result = service.requestSwap(1001);

		assertEquals("Swap request submitted for employee ID 1001", result);
		verify(repository).save(shift);
		logger.info("Swap request submitted for employeeId: {}", 1001);
	}

	@Test
	void testRequestSwap_NotFound() {
		when(repository.findByEmployeeId(1001)).thenReturn(Optional.empty());

		assertThrows(ShiftNotFoundException.class, () -> service.requestSwap(1001));
		logger.warn("Shift not found for employeeId: {}", 1001);
	}

	@Test
	void testProcessSwapRequests() {
		Shift nightShift = new Shift();
		nightShift.setId(2);
		nightShift.setEmployeeId(1002);
		nightShift.setShiftType("Night");
		nightShift.setSwapRequested(true);

		when(repository.findBySwapRequestedTrue()).thenReturn(List.of(shift, nightShift));

		String result = service.processSwapRequests();

		assertEquals("1 Day-Night swaps processed successfully.", result);
		verify(repository, times(2)).save(any(Shift.class));
		logger.info("1 Day-Night swaps processed successfully");
	}

	@Test
	void testApproveSwapByEmployeeId_Success() throws ShiftNotFoundException {
		shift.setSwapRequested(true);
		when(repository.findByEmployeeId(1001)).thenReturn(Optional.of(shift));

		String result = service.approveSwapByEmployeeId(1001);

		assertEquals("Swap approved for employee ID 1001", result);
		verify(repository).save(shift);
		logger.info("Swap approved for employeeId: {}", 1001);
	}

	@Test
	void testApproveSwapByEmployeeId_NotFound() {
		when(repository.findByEmployeeId(1001)).thenReturn(Optional.empty());

		assertThrows(ShiftNotFoundException.class, () -> service.approveSwapByEmployeeId(1001));
		logger.warn("Shift not found for employeeId: {}", 1001);
	}

	@Test
	void testRejectSwapByEmployeeId_Success() {
		shift.setSwapRequested(true);
		when(repository.findByEmployeeId(1001)).thenReturn(Optional.of(shift));

		String result = service.rejectSwapByEmployeeId(1001);

		assertEquals("Swap request rejected for employee ID 1001", result);
		verify(repository).save(shift);
		logger.info("Swap request rejected for employeeId: {}", 1001);
	}

	@Test
	void testRejectSwapByEmployeeId_NotFound() {
		when(repository.findByEmployeeId(1001)).thenReturn(Optional.empty());

		String result = service.rejectSwapByEmployeeId(1001);

		assertEquals("Shift not found for employee ID 1001", result);
		logger.warn("Shift not found for employeeId: {}", 1001);
	}

	@Test
	void testGetShiftsByEmployeeId() {
		List<Shift> shifts = List.of(shift);
		when(repository.findShiftsByEmployeeId(1001)).thenReturn(shifts);

		List<Shift> result = service.getShiftsByEmployeeId(1001);

		assertEquals(1, result.size());
		assertEquals("Day", result.get(0).getShiftType());
		logger.info("Retrieved {} shifts for employeeId: {}", result.size(), 1001);
	}

	@Test
	void testCountShiftsByTypeForEmployee() {
		when(repository.countShiftsByTypeForEmployee(1001))
				.thenReturn(List.of(new Object[] { "Day", 5L }, new Object[] { "Night", 3L }));

		Map<String, Long> result = service.countShiftsByTypeForEmployee(1001);

		assertEquals(2, result.size());
		assertEquals(5L, result.get("Day"));
		assertEquals(3L, result.get("Night"));
		logger.info("Counted shifts by type for employeeId: {}", 1001);
	}

	@Test
	void testGetShiftsByDate() {
		LocalDate date = LocalDate.now();
		List<Shift> shifts = List.of(shift);
		when(repository.getAllShiftsByDate(date)).thenReturn(shifts);

		List<Shift> result = service.getShiftsByDate(date);

		assertEquals(1, result.size());
		assertEquals("Day", result.get(0).getShiftType());
		logger.info("Retrieved {} shifts for date: {}", result.size(), date);
	}
}
