package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.client.AttendanceClient;
import com.example.demo.client.LeaveBalanceClient;
import com.example.demo.client.LeaveManagementClient;
import com.example.demo.client.ShiftClient;
import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.AttendanceMonthlyReportDTO;
import com.example.demo.dto.ShiftDTO;
import com.example.demo.dto.ShiftReportDTO;
import com.example.demo.model.EmployeeReport;

@Service
public class EmployeeReportService {

    @Autowired
    private AttendanceClient attendanceClient;

    @Autowired
    private LeaveManagementClient leaveManagementClient;

    @Autowired
    private LeaveBalanceClient leaveBalanceClient;

    @Autowired
    private ShiftClient shiftClient;

    public EmployeeReport generateReportByEmployeeId(int employeeId) {
        // Fetch data from all services
       // AttendanceDTO attendance = attendanceClient.getAttendanceByEmployeeId(employeeId);
//        List<LeaveRecordDTO> allLeaves = leaveManagementClient.getAllLeaveRecords();
//        List<LeaveBalanceDTO> balances = leaveBalanceClient.getAllBalances();
//        ShiftDTO shift = shiftClient.getShiftByEmployeeId(employeeId);
//
//        // Filter records by employeeId
//        List<LeaveRecordDTO> employeeLeaves = allLeaves.stream()
//                .filter(l -> l.getEmployeeId() == employeeId)
//                .toList();
//
//        LeaveBalanceDTO leaveBalance = balances.stream()
//                .filter(b -> b.getEmployeeId() == employeeId)
//                .findFirst()
//                .orElse(null);
//
//        // Construct report
//        return EmployeeReport.builder()
//                .employeeId(employeeId)
//              //  .attendance(attendance)
//                .leaveBalance(leaveBalance)
//                .leaveRecords(employeeLeaves)
//                .shift(shift)
//                .build();
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


