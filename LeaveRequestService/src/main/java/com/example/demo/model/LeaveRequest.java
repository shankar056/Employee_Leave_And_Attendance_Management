package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leaveId;

	@NotNull(message = "Employee ID is required")
	private int employeeId;

	@NotBlank(message = "Leave type is required")
	private String leaveType;

	@NotNull(message = "Start date is required")
	@FutureOrPresent(message = "Start date must be today or in the future")
	private Date startDate;

	@NotNull(message = "End date is required")
	@FutureOrPresent(message = "End date must be today or in the future")
	private Date endDate;

	private String status;
}
