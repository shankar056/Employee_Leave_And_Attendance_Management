package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int employeeId;
	private String leaveType;
	private Integer balance;
}