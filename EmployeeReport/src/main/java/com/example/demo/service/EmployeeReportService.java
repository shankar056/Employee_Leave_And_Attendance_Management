package com.example.demo.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.example.demo.client.AttendanceClient;
import com.example.demo.client.LeaveBalanceClient;
import com.example.demo.client.LeaveRequestClient;
import com.example.demo.client.ShiftClient;
import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.AttendanceMonthlyReportDTO;
import com.example.demo.dto.ShiftDTO;
import com.example.demo.dto.ShiftReportDTO;
import com.example.demo.model.EmployeeReport;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeReportService {

    private AttendanceClient attendanceClient;
    
    private LeaveRequestClient leaveManagementClient;
    
    private LeaveBalanceClient leaveBalanceClient;

    private ShiftClient shiftClient;

    public EmployeeReport generateReportByEmployeeId(int employeeId) {
    	List<AttendanceDTO> attendance=attendanceClient.getAttendanceByEmployeeId(employeeId);
    	Map<String, Object> monthlyReport=attendanceClient.getDetailedStats(employeeId);
		Map<String, Long> shiftReport=shiftClient.getShiftCountByType(employeeId);
		List<ShiftDTO> shift=shiftClient.getShiftsByEmployeeId(employeeId);
    	return new EmployeeReport(employeeId,
    			new AttendanceMonthlyReportDTO(attendance,monthlyReport),
    			leaveBalanceClient.getBalancesByEmployeeId(employeeId),
    			leaveManagementClient.getLeaveHistoryByEmployeeId(employeeId),
    			new ShiftReportDTO(shift,shiftReport)
    			);
    }
}


