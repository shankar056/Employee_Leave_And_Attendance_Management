package com.example.demo.controller;
 
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Shift;
import com.example.demo.service.ShiftService;
 
@RestController
@RequestMapping("/shifts")
public class ShiftController {
 
    @Autowired
    private ShiftService shiftService;
 
    @GetMapping("findall")
    public List<Shift> getAllShifts() {
        return shiftService.findAll();
    }
 
    @GetMapping("/shiftByShiftId/{id}")
    public Shift getShiftById(@PathVariable int id) {
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
 
    @PostMapping("/requestSwap/{employeeId}")
    public String requestSwap(@PathVariable int employeeId) {
        return shiftService.requestSwap(employeeId);
    }
 
    @PostMapping("/processSwaps")
    public String processSwaps() {
        return shiftService.processSwapRequests();
    }
 
    @PostMapping("/approveSwap/{employeeId}")
    public String approveSwap(@PathVariable int employeeId) {
        return shiftService.approveSwapByEmployeeId(employeeId);
    }
 
    @PostMapping("/rejectSwap/{employeeId}")
    public String rejectSwap(@PathVariable int employeeId) {
        return shiftService.rejectSwapByEmployeeId(employeeId);
    }
    @GetMapping("/employee/{employeeId}")
    public List<Shift> getShiftsByEmployeeId(@PathVariable int employeeId) {
        List<Shift> shifts = shiftService.getShiftsByEmployeeId(employeeId);
        return shifts;
    }
    @GetMapping("/shiftCountByType/{employeeId}")
    public Map<String, Long> getShiftCountByType(@PathVariable int employeeId) {
        return shiftService.countShiftsByTypeForEmployee(employeeId);
    }
    
}