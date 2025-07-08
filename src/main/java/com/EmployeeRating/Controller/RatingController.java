package com.EmployeeRating.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeRating.Dto.RatingDto;
import com.EmployeeRating.Entity.Rating;
import com.EmployeeRating.Service.RatingService;
@RestController
@RequestMapping("/rating")
public class RatingController {
	
	@Autowired
	RatingService ratingService; 
	
	@PostMapping("/save/{empid}")
	public ResponseEntity<?> save(@RequestBody RatingDto dto,@PathVariable(name="empid",required = true) String empid) {
		return ratingService.save(dto,empid);
	}
	
	@GetMapping("/getRating/{id}")
	public Rating getRating(@PathVariable(name="id",required = true) Long id) {
		return ratingService.getRating(id); 
	}
}