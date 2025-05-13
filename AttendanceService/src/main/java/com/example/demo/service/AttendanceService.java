package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Attendance;

public interface AttendanceService {

	Attendance clockIn(int employeeId);

	Attendance clockOut(int employeeId);

	List<Attendance> getAttendanceHistory(int employeeId);

	List<Attendance> getAttendanceByEmployeeId(int employeeId);

}
