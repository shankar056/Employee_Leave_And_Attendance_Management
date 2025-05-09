package com.example.demo.client;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.ShiftDTO;

@FeignClient(name = "SHIFTSERVICE", path = "/shifts")
public interface ShiftClient {

	@GetMapping("/employee/{employeeId}")
    public List<ShiftDTO> getShiftsByEmployeeId(@PathVariable int employeeId);
	@GetMapping("/shiftCountByType/{employeeId}")
    public Map<String, Long> getShiftCountByType(@PathVariable int employeeId);
}
