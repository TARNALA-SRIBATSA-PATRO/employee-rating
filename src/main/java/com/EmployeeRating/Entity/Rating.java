package com.EmployeeRating.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long punctuality;
	
	private Long task_allocation;
	
	private Long teamwork;
	
	private Long adaptability;
	
	private Long communication;
	
	private Long quantity_and_quality;
	
	private Float averageRating;
}
