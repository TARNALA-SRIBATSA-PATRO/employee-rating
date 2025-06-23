package com.EmployeeRating.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
	private Long id;

	private Long punctuality;

	private Long problem_solving;

	private Long teamwork;

	private Long leadership;

	private Long communication;
}
