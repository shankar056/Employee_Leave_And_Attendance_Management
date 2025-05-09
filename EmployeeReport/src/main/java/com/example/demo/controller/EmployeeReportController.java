package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.EmployeeReport;
import com.example.demo.service.EmployeeReportService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/report")
public class EmployeeReportController {

	private EmployeeReportService reportService;

	@GetMapping("/{employeeId}")
	public EmployeeReport getReport(@PathVariable int employeeId) {
		return reportService.generateReportByEmployeeId(employeeId);
	}
}
