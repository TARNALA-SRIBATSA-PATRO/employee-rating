package com.EmployeeRating.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
	
	private Long id;
		
	private String employeeId;
	
	private Long punctuality;
	
	private Long task_allocation;
	
	private Long teamwork;
	
	private Long adaptability;
	
	private Long communication;
	
	private Long quantity_and_quality;
	
	private Float averageRating;
}
