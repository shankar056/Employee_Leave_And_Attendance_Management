package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {
    private int id;
    private int employeeId;
	private String name;
	private String shiftDate;
	private String shiftTime;
	private boolean swapRequested;
}
