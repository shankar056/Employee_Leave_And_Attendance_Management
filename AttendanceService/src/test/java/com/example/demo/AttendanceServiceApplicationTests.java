package com.example.demo;

import com.example.demo.exception.ClockInException;
import com.example.demo.exception.ClockOutException;
import com.example.demo.model.Attendance;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.service.AttendanceServiceImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceServiceApllicationTests {

	private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceApllicationTests.class);

	@Mock
	private AttendanceRepository repo;

	@InjectMocks
	private AttendanceServiceImp service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		logger.info("Setup complete for AttendanceServiceApllicationTests");
	}

	@Test
	void testClockInSuccess() {
		int employeeId = 1;
		LocalDate today = LocalDate.now();

		when(repo.findByEmployeeIdAndDate(employeeId, today)).thenReturn(Optional.empty());
		when(repo.save(any(Attendance.class))).thenAnswer(i -> i.getArgument(0));

		Attendance attendance = service.clockIn(employeeId);

		assertNotNull(attendance);
		assertEquals(employeeId, attendance.getEmployeeId());
		assertEquals(today, attendance.getDate());
		assertNotNull(attendance.getClockIn());

		verify(repo).save(any(Attendance.class));
		logger.info("Employee {} clocked in successfully on {}", employeeId, today);
	}

	@Test
	void testClockInAlreadyExists() {
		int employeeId = 1;
		LocalDate today = LocalDate.now();
		Attendance mockAttendance = new Attendance();
		mockAttendance.setEmployeeId(employeeId);
		mockAttendance.setDate(today);

		when(repo.findByEmployeeIdAndDate(employeeId, today)).thenReturn(Optional.of(mockAttendance));

		assertThrows(ClockInException.class, () -> service.clockIn(employeeId));
		logger.error("Clock-in attempt failed: Employee {} has already clocked in for today.", employeeId);
	}

	@Test
	void testClockOutSuccess() {
		int employeeId = 1;
		LocalDate today = LocalDate.now();
		LocalDateTime clockIn = LocalDateTime.now().minusHours(8);

		Attendance attendance = new Attendance();
		attendance.setEmployeeId(employeeId);
		attendance.setDate(today);
		attendance.setClockIn(clockIn);

		when(repo.findByEmployeeIdAndDate(employeeId, today)).thenReturn(Optional.of(attendance));
		when(repo.save(any(Attendance.class))).thenAnswer(i -> i.getArgument(0));

		Attendance result = service.clockOut(employeeId);

		assertNotNull(result.getClockOut());
		assertEquals(8L, result.getWorkHours());
		logger.info("Employee {} clocked out successfully on {} with {} hours worked.", employeeId, today,
				result.getWorkHours());
	}

	@Test
	void testClockOutNoClockIn() {
		int employeeId = 1;
		LocalDate today = LocalDate.now();

		when(repo.findByEmployeeIdAndDate(employeeId, today)).thenReturn(Optional.empty());

		assertThrows(ClockOutException.class, () -> service.clockOut(employeeId));
		logger.error("Clock-out attempt failed: No clock-in record found for employee {} today.", employeeId);
	}

	@Test
	void testGetDetailedAttendanceStats() {
		int employeeId = 1;
		List<Attendance> records = new ArrayList<>();

		// Create attendance for four weeks in a single month
		LocalDateTime now = LocalDateTime.now();
		for (int i = 1; i <= 28; i += 7) {
			Attendance att = new Attendance();
			att.setEmployeeId(employeeId);
			att.setDate(LocalDate.of(now.getYear(), now.getMonth(), i));
			att.setClockIn(LocalDateTime.of(now.getYear(), now.getMonth(), i, 9, 0));
			att.setClockOut(LocalDateTime.of(now.getYear(), now.getMonth(), i, 17, 0));
			att.setWorkHours(8L);
			records.add(att);
		}

		when(repo.findByEmployeeId(employeeId)).thenReturn(records);

		Map<String, Object> result = service.getDetailedAttendanceStats(employeeId);

		assertFalse(result.isEmpty());
		String month = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
		assertTrue(result.containsKey(month));

		Map<String, Object> monthDetails = (Map<String, Object>) result.get(month);
		assertEquals(4L, monthDetails.get("TotalDays"));
		assertEquals(8.0, monthDetails.get("MonthlyAverageHours"));

		Map<String, Double> weekly = (Map<String, Double>) monthDetails.get("WeeklyAverageHours");
		assertEquals(4, weekly.size());

		logger.info("Detailed attendance stats for employee {}: {}", employeeId, result);
	}
}
