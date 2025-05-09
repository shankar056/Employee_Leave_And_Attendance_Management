package com.example.demo.model;

import java.util.List;

import com.example.demo.dto.AttendanceMonthlyReportDTO;
import com.example.demo.dto.LeaveBalanceDTO;
import com.example.demo.dto.LeaveRecordDTO;
import com.example.demo.dto.ShiftReportDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReport {
    
	private int employeeId;
    private AttendanceMonthlyReportDTO attendanceReport;
    private List<LeaveBalanceDTO> leaveBalance;
    private List<LeaveRecordDTO> leaveRecords;
    ShiftReportDTO shift;
}