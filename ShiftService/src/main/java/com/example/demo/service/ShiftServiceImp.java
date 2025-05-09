package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.example.demo.model.Shift;
import com.example.demo.repository.ShiftRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ShiftServiceImp implements ShiftService {

	private ShiftRepository repository;

	@Override
	public List<Shift> findAll() {
		return repository.findAll();
	}

	@Override
	public Shift findById(int id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("Shift not found"));
	}

	@Override
	public void save(Shift shift) {
		repository.save(shift);
	}

	@Override
	public String deleteById(int id) {
		repository.deleteById(id);
		return "Shift Deleted";
	}

	@Override
	public String requestSwap(int employeeId) {
		Optional<Shift> optionalShift = repository.findByEmployeeId(employeeId);
		if (optionalShift.isPresent()) {
			Shift shift = optionalShift.get();
			shift.setSwapRequested(true);
			repository.save(shift);
			return "Swap request submitted for employee ID " + employeeId;
		}
		return "Shift not found for employee ID " + employeeId;
	}

	@Override
	public String processSwapRequests() {
		List<Shift> requestedShifts = repository.findBySwapRequestedTrue();

		// Separate Day and Night shift requests
		List<Shift> dayShifts = new ArrayList<>();
		List<Shift> nightShifts = new ArrayList<>();

		for (Shift shift : requestedShifts) {
			if ("Day".equalsIgnoreCase(shift.getShiftType())) {
				dayShifts.add(shift);
			} else if ("Night".equalsIgnoreCase(shift.getShiftType())) {
				nightShifts.add(shift);
			}
		}

		int swapCount = Math.min(dayShifts.size(), nightShifts.size());
		if (swapCount == 0) {
			return "No matching Day-Night swap requests available.";
		}

		for (int i = 0; i < swapCount; i++) {
			Shift day = dayShifts.get(i);
			Shift night = nightShifts.get(i);

			// Swap only the shift types
			String temp = day.getShiftType();
			day.setShiftType(night.getShiftType());
			night.setShiftType(temp);

			// Reset swapRequested
			day.setSwapRequested(false);
			night.setSwapRequested(false);

			repository.save(day);
			repository.save(night);
		}

		return swapCount + " Day-Night swaps processed successfully.";
	}

	@Override
	public String approveSwapByEmployeeId(int employeeId) {
		Optional<Shift> optionalShift = repository.findByEmployeeId(employeeId);
		if (optionalShift.isPresent()) {
			Shift shift = optionalShift.get();
			if (shift.isSwapRequested()) {
				shift.setSwapRequested(false);
				repository.save(shift);
				return "Swap approved for employee ID " + employeeId;
			} else {
				return "No swap request found for employee ID " + employeeId;
			}
		}
		return "Shift not found for employee ID " + employeeId;
	}

	@Override
	public String rejectSwapByEmployeeId(int employeeId) {
		Optional<Shift> optionalShift = repository.findByEmployeeId(employeeId);
		if (optionalShift.isPresent()) {
			Shift shift = optionalShift.get();
			if (shift.isSwapRequested()) {
				shift.setSwapRequested(false);
				repository.save(shift);
				return "Swap request rejected for employee ID " + employeeId;
			} else {
				return "No swap request found for employee ID " + employeeId;
			}
		}
		return "Shift not found for employee ID " + employeeId;
	}

	public List<Shift> getShiftsByEmployeeId(int employeeId) {
		return repository.findShiftsByEmployeeId(employeeId);
	}

	@Override
	public Map<String, Long> countShiftsByTypeForEmployee(int employeeId) {
		List<Object[]> results = repository.countShiftsByTypeForEmployee(employeeId);
		Map<String, Long> map = new HashMap<>();
		for (Object[] row : results) {
			String type = (String) row[0];
			Long count = (Long) row[1];
			map.put(type, count);
		}
		return map;
	}
}