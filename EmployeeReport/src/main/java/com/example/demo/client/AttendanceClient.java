package com.example.demo.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.AttendanceDTO;

@FeignClient(name = "ATTENDANCESERVICE", path = "/attendance")
public interface AttendanceClient {
    @GetMapping("/employee/{employeeId}")
    List<AttendanceDTO> getAttendanceByEmployeeId(@PathVariable int employeeId);
    @GetMapping("/detailed-stats/{employeeId}")
	public Map<String, Object> getDetailedStats(@PathVariable int employeeId);
}
