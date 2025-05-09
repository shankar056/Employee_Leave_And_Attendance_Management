package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.exception.ClockInandClockOutException;
import com.example.demo.model.Attendance;

public interface AttendanceService {

	Attendance clockIn(int employeeId) throws ClockInandClockOutException;

	Attendance clockOut(int employeeId);

	List<Attendance> getAttendanceHistory(int employeeId);

	List<Attendance> getAttendanceByEmployeeId(int employeeId);

}
