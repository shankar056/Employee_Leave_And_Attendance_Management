package com.example.demo.repository;

import com.example.demo.model.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Integer> {
	List<LeaveBalance> findByEmployeeId(int employeeId);

	Optional<LeaveBalance> findByEmployeeIdAndLeaveType(long employeeId, String leaveType);
}