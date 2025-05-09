package com.example.demo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LeaveRecordDTO;

@FeignClient(name = "LEAVEREQUESTSERVICE", path = "/leaverequest")
public interface LeaveRequestClient {

    @GetMapping("/employee/{employeeId}")
    List<LeaveRecordDTO> getLeaveHistoryByEmployeeId(@PathVariable long employeeId);
}
