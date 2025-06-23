package com.EmployeeRating.Dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRatingTrackerDto {
	private Long id;

	private LocalDate sendDate;

	private LocalDate submitDate;
}
