package com.example.demo.service;
 
import com.example.demo.model.Shift;
import java.util.List;
import java.util.Map;
 
public interface ShiftService {
    List<Shift> findAll();
    Shift findById(int id);
    void save(Shift shift);
    String deleteById(int id);
    String requestSwap(int employeeId);
    String processSwapRequests();
    String approveSwapByEmployeeId(int employeeId);
    String rejectSwapByEmployeeId(int employeeId);
	List<Shift> getShiftsByEmployeeId(int employeeId);
	Map<String, Long> countShiftsByTypeForEmployee(int employeeId);
}