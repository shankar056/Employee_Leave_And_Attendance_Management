package com.example.demo.service;

import java.time.LocalDate;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.exception.*;
import com.example.demo.model.Shift;
import com.example.demo.repository.ShiftRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ShiftServiceImp implements ShiftService {

	private static final Logger logger = LoggerFactory.getLogger(ShiftServiceImp.class);

	private ShiftRepository repository;

	private static final String DAY_SHIFT = "Day";
	private static final String NIGHT_SHIFT = "Night";
	private static final String SHIFT_NOT_FOUND = "Shift not found";
	private static final String SHIFT_DELETED = "Shift Deleted";
	private static final String NO_SWAP_REQUEST_FOUND = "No swap request found for employee ID %d";
	private static final String SWAP_REQUEST_SUBMITTED = "Swap request submitted for employee ID %d";
	private static final String SWAP_APPROVED = "Swap approved for employee ID %d";
	private static final String SWAP_REJECTED = "Swap request rejected for employee ID %d";
	private static final String NO_MATCHING_SWAP_REQUESTS = "No matching Day-Night swap requests available.";

	@Override
	public List<Shift> findAll() {
		logger.info("Fetching all shifts");
		return repository.findAll();
	}

	@Override
	public Shift findById(int id) throws ShiftNotFoundException {
		logger.info("Fetching shift by ID: {}", id);
		return repository.findById(id).orElseThrow(() -> {
			logger.warn("Shift not found with ID: {}", id);
			return new ShiftNotFoundException(SHIFT_NOT_FOUND);
		});
	}

	@Override
	public void save(Shift shift) {
		logger.info("Saving shift for employeeId: {}", shift.getEmployeeId());

		// Prevent past date shift creation
		if (shift.getDate().isBefore(LocalDate.now())) {
			logger.warn("Attempt to save shift with past date: {}", shift.getDate());
			throw new ShiftDateInPastException("Cannot create shift for past date.");
		}

		// Validate shift type
		if (!DAY_SHIFT.equalsIgnoreCase(shift.getShiftType()) && !NIGHT_SHIFT.equalsIgnoreCase(shift.getShiftType())) {
			logger.warn("Invalid shift type: {}", shift.getShiftType());
			throw new InvalidShiftTypeException("Shift type must be 'Day' or 'Night'.");
		}

		// Check for duplicate (same employee, same date)
		List<Shift> existing = repository.findShiftsByEmployeeId(shift.getEmployeeId());
		for (Shift s : existing) {
			if (s.getDate().equals(shift.getDate())) {
				logger.warn("Duplicate shift for employeeId {} on {}", shift.getEmployeeId(), shift.getDate());
				throw new DuplicateShiftException("Shift already exists for this employee on this date.");
			}
		}

		repository.save(shift);
	}

	@Override
	public String deleteById(int id) {
		logger.info("Deleting shift with ID: {}", id);
		repository.deleteById(id);
		return SHIFT_DELETED;
	}

	@Override
    public String requestSwap(int id) throws ShiftNotFoundException {
        logger.info("Requesting shift swap for Id: {}", id);
        Optional<Shift> optionalShift = repository.findById(id);
        if (optionalShift.isPresent()) {
            Shift shift = optionalShift.get();
            shift.setSwapRequested(true);
            repository.save(shift);
            logger.info("Swap request submitted for id: {}", id);
            return "Swap request submitted for ID " + id;
        } else {
            logger.warn("Shift not found for id: {}", id);
            throw new ShiftNotFoundException("Shift not found for id  " + id);
        }
    }
 
	@Override
    public String processSwapRequests(LocalDate date) {
 
        logger.info("Processing shift swap requests for date: {}", date);
 
        List<Shift> requestedShifts = repository.findBySwapRequestedTrue();
 
        // Filter shifts by the given date
        requestedShifts = requestedShifts.stream()
                .filter(shift -> date.equals(shift.getDate()))
                .toList();
 
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
            logger.info("No matching Day-Night swap requests available for date: {}", date);
            return "No matching Day-Night swap requests available for " + date + ".";
        }
 
        for (int i = 0; i < swapCount; i++) {
            Shift day = dayShifts.get(i);
            Shift night = nightShifts.get(i);
 
            String temp = day.getShiftType();
            day.setShiftType(night.getShiftType());
            night.setShiftType(temp);
 
            day.setSwapRequested(false);
            night.setSwapRequested(false);
 
            repository.save(day);
            repository.save(night);
        }
 
        logger.info("{} Day-Night swaps processed successfully for date {}", swapCount, date);
        return swapCount + " Day-Night swaps processed successfully for " + date + ".";
    }
 

	@Override
    public String approveSwapByEmployeeId(int employeeId) throws ShiftNotFoundException {
        logger.info("Approving swap for employeeId: {}", employeeId);
        Optional<Shift> optionalShift = repository.findById(employeeId);
        if (optionalShift.isPresent()) {
            Shift shift = optionalShift.get();
            if (shift.isSwapRequested()) {
                shift.setSwapRequested(false);
                String type=shift.getShiftType();
                shift.setShiftType((type.equals("Day")?"Night":"Day"));
                repository.save(shift);
                logger.info("Swap approved for employeeId: {}", employeeId);
                return "Swap approved for employee ID " + employeeId;
            } else {
                logger.info("No swap request found for employeeId: {}", employeeId);
                return "No swap request found for employee ID " + employeeId;
            }
        } else {
            logger.warn("Shift not found for employeeId: {}", employeeId);
            throw new ShiftNotFoundException("Shift not found for employee ID " + employeeId);
        }
    }
	
	   @Override
	    public String rejectSwapByEmployeeId(int employeeId) {
	        logger.info("Rejecting swap for employeeId: {}", employeeId);
	        Optional<Shift> optionalShift = repository.findById(employeeId);
	        if (optionalShift.isPresent()) {
	            Shift shift = optionalShift.get();
	            if (shift.isSwapRequested()) {
	                shift.setSwapRequested(false);
	                repository.save(shift);
	                logger.info("Swap request rejected for employeeId: {}", employeeId);
	                return "Swap request rejected for employee ID " + employeeId;
	            } else {
	                logger.info("No swap request found for employeeId: {}", employeeId);
	                return "No swap request found for employee ID " + employeeId;
	            }
	        }
	        logger.warn("Shift not found for employeeId: {}", employeeId);
	        return "Shift not found for employee ID " + employeeId;
	    }

	@Override
	public List<Shift> getShiftsByEmployeeId(int employeeId) {
		logger.info("Fetching shifts for employeeId: {}", employeeId);
		return repository.findShiftsByEmployeeId(employeeId);
	}

	@Override
	public Map<String, Long> countShiftsByTypeForEmployee(int employeeId) {
		logger.info("Counting shifts by type for employeeId: {}", employeeId);
		List<Object[]> results = repository.countShiftsByTypeForEmployee(employeeId);
		Map<String, Long> map = new HashMap<>();
		for (Object[] row : results) {
			String type = (String) row[0];
			Long count = (Long) row[1];
			map.put(type, count);
		}
		return map;
	}

	@Override
	public List<Shift> getShiftsByDate(LocalDate date) {
		logger.info("Fetching all shifts for date: {}", date);
		return repository.getAllShiftsByDate(date);
	}
}