package com.example.demo.feignclient;

import com.example.demo.dto.LeaveBalanceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "LEAVEBALANCESERVICE", path = "/leavebalance")
public interface BalanceLeave {

	@GetMapping("/{employeeId}/{leaveType}")
	LeaveBalanceDTO getBalanceByType(@PathVariable int employeeId, @PathVariable String leaveType);

	@PutMapping("/update")
	void updateLeaveBalance(@RequestBody LeaveBalanceDTO balance);
}