package com.EmployeeRating.Dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormData {
	List<IndividualData> employees;
	private String projectManagerName;
	private String projectManagerEmail;
	private String projectName;
	private String pmoName;
	private String pmoEmail;
	private String teamLeadEmail;
	private String teamLeadName;
	private LocalDate projectStartDate;
	private LocalDate projectEndDate;
}
