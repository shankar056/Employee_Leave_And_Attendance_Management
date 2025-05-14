package com.example.demo.service;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(EmployeeReportService.class);

    private AttendanceClient attendanceClient;
    private LeaveRequestClient leaveManagementClient;
    private LeaveBalanceClient leaveBalanceClient;
    private ShiftClient shiftClient;

    public EmployeeReport generateReportByEmployeeId(int employeeId) {
        logger.info("Generating report for employeeId: {}", employeeId);

        List<AttendanceDTO> attendance = attendanceClient.getAttendanceByEmployeeId(employeeId);
        logger.info("Retrieved {} attendance records for employeeId: {}", attendance.size(), employeeId);

        Map<String, Object> monthlyReport = attendanceClient.getDetailedStats(employeeId);
        logger.info("Retrieved detailed attendance stats for employeeId: {}", employeeId);

        Map<String, Long> shiftReport = shiftClient.getShiftCountByType(employeeId);
        logger.info("Retrieved shift count by type for employeeId: {}", employeeId);

        List<ShiftDTO> shift = shiftClient.getShiftsByEmployeeId(employeeId);
        logger.info("Retrieved {} shift records for employeeId: {}", shift.size(), employeeId);

        EmployeeReport report = new EmployeeReport(
            employeeId,
            new AttendanceMonthlyReportDTO(attendance, monthlyReport),
            leaveBalanceClient.getBalancesByEmployeeId(employeeId),
            leaveManagementClient.getLeaveHistoryByEmployeeId(employeeId),
            new ShiftReportDTO(shift, shiftReport)
        );

        logger.info("Generated report for employeeId: {}", employeeId);
        return report;
    }
}
