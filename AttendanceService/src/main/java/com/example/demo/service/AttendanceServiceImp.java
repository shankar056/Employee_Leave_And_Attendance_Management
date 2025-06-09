package com.example.demo.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ClockInException;
import com.example.demo.exception.ClockOutException;
import com.example.demo.model.Attendance;
import com.example.demo.repository.AttendanceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AttendanceServiceImp implements AttendanceService {
	private AttendanceRepository repo;
	private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImp.class);

	public Attendance clockIn(int employeeId) {
		LocalDate today = LocalDate.now();
		repo.findByEmployeeIdAndDate(employeeId, today).ifPresent(a -> {
			logger.error("Clock-in attempt failed: Employee {} has already clocked in for today.", employeeId);
			throw new ClockInException("Employee has already clocked in for today.");
		});

		Attendance attendance = new Attendance();
		attendance.setEmployeeId(employeeId);
		attendance.setDate(today);
		attendance.setClockIn(LocalDateTime.now());

		Attendance savedAttendance = repo.save(attendance);
		logger.info("Employee {} clocked in at {}", employeeId, savedAttendance.getClockIn());
		return savedAttendance;

	}

	public Attendance clockOut(int employeeId) {
		LocalDate today = LocalDate.now();
		Attendance attendance = repo.findByEmployeeIdAndDate(employeeId, today).orElseThrow(() -> {
			logger.error("Clock-out attempt failed: No clock-in record found for employee {} today.", employeeId);
			return new ClockOutException("No clock-in record found for today.");
		});

		attendance.setClockOut(LocalDateTime.now());
		attendance.setWorkHours(calculateWorkHours(attendance.getClockIn(), attendance.getClockOut()));

		Attendance savedAttendance = repo.save(attendance);
		logger.info("Employee {} clocked out at {} with {} hours worked.", employeeId, savedAttendance.getClockOut(),
				savedAttendance.getWorkHours());
		return savedAttendance;

	}
	public List<Attendance> getAllAttendances() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	public List<Attendance> getAttendanceHistory(int employeeId) {

		List<Attendance> attendanceHistory = repo.findAllByEmployeeId(employeeId);
		logger.info("Retrieved attendance history for employee {}: {} records found.", employeeId,
				attendanceHistory.size());
		return attendanceHistory;

	}

	private Long calculateWorkHours(LocalDateTime clockIn, LocalDateTime clockOut) {
		Duration duration = Duration.between(clockIn, clockOut);
		return duration.toHours();
	}

	@Override
	public List<Attendance> getAttendanceByEmployeeId(int employeeId) {
		return repo.findByEmployeeId(employeeId);
	}

	public Map<String, Object> getDetailedAttendanceStats(int employeeId) {
		List<Attendance> records = repo.findByEmployeeId(employeeId);
		logger.info("Retrieved {} attendance records for employee {}.", records.size(), employeeId);

		List<Attendance> validRecords = records.stream().filter(a -> a.getClockIn() != null && a.getClockOut() != null)
				.collect(Collectors.toList());

		Map<String, Object> result = new LinkedHashMap<>();

		Map<String, List<Attendance>> byMonth = validRecords.stream()
				.collect(Collectors.groupingBy(a -> a.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
						LinkedHashMap::new, Collectors.toList()));

		for (Map.Entry<String, List<Attendance>> entry : byMonth.entrySet()) {
			String month = entry.getKey();
			List<Attendance> monthlyRecords = entry.getValue();

			long totalDays = monthlyRecords.size();
			double totalHours = monthlyRecords.stream().mapToLong(Attendance::getWorkHours).sum();

			double monthlyAvgHours = totalDays == 0 ? 0 : totalHours / totalDays;

// Weekly calculation
			Map<String, Double> weeklyAvgMap = new LinkedHashMap<>();
			Map<Integer, List<Attendance>> byWeek = monthlyRecords.stream().collect(Collectors.groupingBy(a -> {
				int day = a.getDate().getDayOfMonth();
				return (day - 1) / 7 + 1; // Week 1 to 4
			}, TreeMap::new, Collectors.toList()));
			for (int week = 1; week <= 4; week++) {
				List<Attendance> weekRecords = byWeek.getOrDefault(week, new ArrayList<>());
				double weekHours = weekRecords.stream().mapToLong(Attendance::getWorkHours).sum();
				double avg = weekRecords.isEmpty() ? 0.0 : weekHours / weekRecords.size();
				weeklyAvgMap.put("Week " + week, avg);
			}
			Map<String, Object> monthDetails = new LinkedHashMap<>();
			monthDetails.put("TotalDays", totalDays);
			monthDetails.put("MonthlyAverageHours", monthlyAvgHours);
			monthDetails.put("WeeklyAverageHours", weeklyAvgMap);

			result.put(month, monthDetails);
			logger.info(
					"Processed attendance stats for month {}: TotalDays={}, MonthlyAverageHours={}, WeeklyAverageHours={}",
					month, totalDays, monthlyAvgHours, weeklyAvgMap);
		}

		return result;

	}

}
