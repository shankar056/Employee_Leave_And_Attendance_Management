package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.LeaveTypeNotFoundException;
import com.example.demo.model.LeaveRequest;
import com.example.demo.service.LeaveRequestServiceImp;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/leaverequest")
@AllArgsConstructor
public class LeaveRequestController {
    LeaveRequestServiceImp service;

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody LeaveRequest req)  {
        return ResponseEntity.ok(service.applyLeave(req));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approve(@PathVariable int id) {
        return ResponseEntity.ok(service.approveLeave(id));
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<String> reject(@PathVariable int id) {
        return ResponseEntity.ok(service.rejectLeave(id));
    }

    @GetMapping("/history")
    public List<LeaveRequest> all() {
        return service.getAllRequests();
    }

    @GetMapping("/history/{status}")
    public List<LeaveRequest> byStatus(@PathVariable String status) {
        return service.getRequestsByStatus(status);
    }
    
    @GetMapping("/employee/{employeeId}")
    public List<LeaveRequest> getLeaveHistoryByEmployeeId(@PathVariable int employeeId) {
        return service.getRequestsByEmployeeId(employeeId);
    }
 
    @DeleteMapping("/delete/{id}")
	public String deleteLeaveRequest(@PathVariable int id) {
		return service.deleteLeaveRequest(id);
	}
}