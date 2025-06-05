package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Attendance;
import com.example.demo.service.AttendanceServiceImp;

import lombok.AllArgsConstructor;

@RestController

@RequestMapping("/attendance")

@AllArgsConstructor
public class AttendanceController {

	private AttendanceServiceImp service;

	// http://localhost:1001/attendance/clockin

	@PostMapping("/clockin")

	public Attendance clockIn(@RequestParam int employeeId)  {

		return service.clockIn(employeeId);
 
	}

	// http://localhost:1001/attendance/clockout

	@PostMapping("/clockout")

	public Attendance clockOut(@RequestParam int employeeId) {

		return service.clockOut(employeeId);

	}

	// http://localhost:1001/attendance/history

	@GetMapping("/history")

	public List<Attendance> getAttendanceHistory(@RequestParam int employeeId) {

		return service.getAttendanceHistory(employeeId);

	}

	@GetMapping("/employee/{employeeId}")
	public List<Attendance> getAttendanceByEmployeeId(@PathVariable ("employeeId") int employeeId) {
		return service.getAttendanceByEmployeeId(employeeId);
	}
	
	@GetMapping("/detailed-stats/{employeeId}")
	public Map<String, Object> getDetailedStats(@PathVariable int employeeId) {
		return service.getDetailedAttendanceStats(employeeId);
	}
	//List<Attendance> getAttendanceByEmployeeId

}
