package com.example.demo.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftReportDTO {
	private List<ShiftDTO> shift;
	Map<String, Long> shiftReport;
}
