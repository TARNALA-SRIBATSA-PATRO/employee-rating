package com.EmployeeRating.Dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualData {
	private Long id;
	private String employeeId;
	private String employeeName;
	private String employeeEmail;
	private String department;
	private String employmentType;
	private LocalDate joiningDate;
	private String designation;

}
