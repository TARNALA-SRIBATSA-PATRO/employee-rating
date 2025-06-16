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
	private String name;
	private String email;
	private String projectManager;
	private String projectManagerEmail;
	private LocalDate startDate;
	private LocalDate endDate;
}
