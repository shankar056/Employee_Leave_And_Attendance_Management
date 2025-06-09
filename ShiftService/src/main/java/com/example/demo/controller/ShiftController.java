package com.example.demo.controller;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.demo.exception.ShiftNotFoundException;
import com.example.demo.model.Shift;
import com.example.demo.service.ShiftService;

import lombok.AllArgsConstructor;
 
@RestController
@AllArgsConstructor
@RequestMapping("/shifts")
public class ShiftController {
	
    private ShiftService shiftService;
 
    @GetMapping("findall")
    public List<Shift> getAllShifts() {
        return shiftService.findAll();
    }
 
    @GetMapping("/shiftByShiftId/{id}")
    public Shift getShiftById(@PathVariable int id) throws ShiftNotFoundException {
        return shiftService.findById(id);
    }
 
    @PostMapping("/save")
    public void createShift(@RequestBody Shift shift) {
        shiftService.save(shift);
    }
 
    @DeleteMapping("/delete/{id}")
    public String deleteShift(@PathVariable int id) {
        return shiftService.deleteById(id);
    }
 
    @PostMapping("/requestSwap/{shiftId}")
    public String requestSwap(@PathVariable int shiftId) throws ShiftNotFoundException {
        return shiftService.requestSwap(shiftId);
    }
 
    @PostMapping("/processSwaps")
    public String processSwaps() {
        return shiftService.processSwapRequests();
    }
 
    @PostMapping("/approveSwap/{employeeId}")
    public String approveSwap(@PathVariable int employeeId) throws ShiftNotFoundException {
        return shiftService.approveSwapByEmployeeId(employeeId);
    }
 
    @PostMapping("/rejectSwap/{employeeId}")
    public String rejectSwap(@PathVariable int employeeId) {
        return shiftService.rejectSwapByEmployeeId(employeeId);
    }
 
    @GetMapping("/shiftByEmployeeId/{employeeId}")
    public List<Shift> getShiftsByEmployeeId(@PathVariable int employeeId) {
        return shiftService.getShiftsByEmployeeId(employeeId);
    }
 
    @GetMapping("/shiftCountByType/{employeeId}")
    public Map<String, Long> getShiftCountByType(@PathVariable int employeeId) {
        return shiftService.countShiftsByTypeForEmployee(employeeId);
    }
 
    @GetMapping("/byDate/{date}")
    public List<Shift> getShiftsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return shiftService.getShiftsByDate(date);
    }
 
}
 