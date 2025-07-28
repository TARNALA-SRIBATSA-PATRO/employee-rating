package com.EmployeeRating.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.EmployeeRating.Dto.RatingDto;
import com.EmployeeRating.Entity.Rating;

public interface RatingService {

	ResponseEntity<?> save(RatingDto dto,String empid);

	Rating getRating(Long id);

	ResponseEntity<?> update(RatingDto dto, String employeeId);

	ResponseEntity<?> update(List<RatingDto> dtoList);

    ResponseEntity<?> getRatingsByEmployeeIds(List<String> employeeIds);
    ResponseEntity<?> bulkSaveRatings(List<RatingDto> dtos);
}
