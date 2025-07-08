package com.EmployeeRating.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.EmployeeRating.Dto.EmployeeDto;

public interface EmployeeService {

	ResponseEntity<?> save(List<EmployeeDto> dto);

	ResponseEntity<?> fetchAll();

	ResponseEntity<?> getEmployee(LocalDate date);

	ResponseEntity<?> getByCriteria(String managerEmail);
	
	ResponseEntity<?> deleteDetails(String id);

}
