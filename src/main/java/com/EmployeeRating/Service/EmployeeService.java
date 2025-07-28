package com.EmployeeRating.Service;

import java.time.LocalDate;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;

import com.EmployeeRating.Dto.EmployeeDto;
import com.EmployeeRating.Dto.FormData;

public interface EmployeeService {

	//ResponseEntity<?> save(List<EmployeeDto> dto);

	ResponseEntity<?> fetchAll();

	ResponseEntity<?> getEmployee(LocalDate date);

	ResponseEntity<?> getByCriteria(String managerEmail);
	
	ResponseEntity<?> deleteDetails(String id);

	byte[] generateEmployeeExcel(Long employeeId);

	byte[] generateEmployeesExcel(String manager) throws InvalidFormatException;
	
	ResponseEntity<?> save(List<EmployeeDto> dto);
	ResponseEntity<?> save(FormData formData);

	byte[] generateEmployeesExcelForManagerOfficer(String managerOfficer);

	byte[] generateEmployeesExcelHr();

    List<String> getAllEmployeeIds();
}
