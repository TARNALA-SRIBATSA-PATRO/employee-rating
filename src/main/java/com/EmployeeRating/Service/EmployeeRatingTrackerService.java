package com.EmployeeRating.Service;

import org.springframework.http.ResponseEntity;

import com.EmployeeRating.Dto.EmployeeRatingTrackerDto;

public interface EmployeeRatingTrackerService {

	ResponseEntity<?> save(EmployeeRatingTrackerDto dto);

	void tlSubmit(String employeeId);

	void pmSubmit(String employeeId);
	
	void pmoSubmit(String employeeId);

}
