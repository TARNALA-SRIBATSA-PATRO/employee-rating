package com.EmployeeRating.Service;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;

import com.EmployeeRating.Dto.EmployeeDto;
import com.EmployeeRating.Entity.Employee;

public interface EmployeeService {

	EmployeeDto save(Employee employee);

	ResponseEntity<?> fetchAll();

	ResponseEntity<?> getEmployee(LocalDate date);

	ResponseEntity<?> getByCriteria(String managerEmail);

}
