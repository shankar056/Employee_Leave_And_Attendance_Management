package com.example.demo.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Shift;
 
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    Optional<Shift> findByEmployeeId(int employeeId);
    List<Shift> findBySwapRequestedTrue();
    @Query("SELECT s FROM Shift s WHERE s.employeeId = :employeeId")
    List<Shift> findShiftsByEmployeeId(@Param("employeeId") int employeeId);
    @Query("SELECT s.shiftType, COUNT(s) FROM Shift s WHERE s.employeeId = :employeeId GROUP BY s.shiftType")
    List<Object[]> countShiftsByTypeForEmployee(@Param("employeeId") int employeeId);
}