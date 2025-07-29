package com.EmployeeRating.Dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
	
	private Long id;
	private String employeeId;
	private String employeeName;
	private String employeeEmail;
	private String projectManager;
	private String projectManagerEmail;
	private String projectName;
	private boolean pmSubmitted;
	private LocalDate startDate;
	private LocalDate endDate;
	private String teamLead;
	private String teamLeadEmail;
	private boolean isTLSubmitted;
	private boolean isHrSend;
	private String pmoName;
	private String pmoEmail;
	private String designation;
	private String department;
	private String employmentType;
	private boolean isPmoSubmitted;
	private LocalDate joiningDate;
	private LocalDate leaveDate;
}