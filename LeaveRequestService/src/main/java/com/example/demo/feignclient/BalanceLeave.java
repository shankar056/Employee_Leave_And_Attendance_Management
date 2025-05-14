package com.example.demo.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.dto.LeaveBalanceDTO;

@FeignClient(value = "LEAVEBALANCESERVICE", path = "/leavebalance")
public interface BalanceLeave {

	@GetMapping("/{employeeId}/{leaveType}")
	LeaveBalanceDTO getBalanceByType(@PathVariable int employeeId, @PathVariable String leaveType);

	@PutMapping("/update")
	void updateLeaveBalance(@RequestBody LeaveBalanceDTO balance);
}