package com.EmployeeRating.Service;

import org.springframework.http.ResponseEntity;

import com.EmployeeRating.Dto.RatingDto;
import com.EmployeeRating.Entity.Rating;

public interface RatingService {

	ResponseEntity<?> save(RatingDto dto,String empid);

	Rating getRating(Long id);

}
