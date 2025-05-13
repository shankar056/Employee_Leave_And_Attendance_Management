	package com.example.demo.controller;

import com.example.demo.exception.LeaveTypeNotFound;
import com.example.demo.model.LeaveBalance;
import com.example.demo.service.LeaveBalanceServiceImp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leavebalance")
@RequiredArgsConstructor
public class LeaveBalanceController {

	private final LeaveBalanceServiceImp leaveBalanceService;

	@PostMapping("/initialize/{employeeId}")
    public ResponseEntity<String> init(@PathVariable int employeeId) {
		leaveBalanceService.initializeLeaveBalance(employeeId);
        return ResponseEntity.ok("Initialized");
    }
	@PutMapping("/update")
    public void updateBalance(@RequestBody LeaveBalance balance) throws LeaveTypeNotFound{
        leaveBalanceService.updateLeaveBalance(balance);
    }
	
	@GetMapping("/{employeeId}/{leaveType}")
    public ResponseEntity<LeaveBalance> getBalanceByType(@PathVariable int employeeId, @PathVariable String leaveType) {
        return leaveBalanceService.getBalanceByType(employeeId, leaveType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
	
	@GetMapping("/employee/{employeeId}")
    public List<LeaveBalance> getBalancesByEmployeeId(@PathVariable int employeeId) {
        return leaveBalanceService.getLeaveBalancesByEmployeeId(employeeId);
    }

	
}
