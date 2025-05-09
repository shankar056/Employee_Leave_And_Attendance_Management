package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDTO {
	private int id;
	private int employeeId;
	private String leaveType;
	private Integer balance;
}
