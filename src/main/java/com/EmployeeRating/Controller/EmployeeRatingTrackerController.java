package com.EmployeeRating.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeRating.Dto.EmployeeRatingTrackerDto;
import com.EmployeeRating.Service.EmployeeRatingTrackerService;

@RestController
@RequestMapping("/tracking")
public class EmployeeRatingTrackerController {
	@Autowired
	EmployeeRatingTrackerService EmployeeRatingTrackerService;
	@PostMapping("/save")
	public ResponseEntity<?> saveTracking(@RequestBody EmployeeRatingTrackerDto dto) {
		return EmployeeRatingTrackerService.save(dto);
	}
	
}
