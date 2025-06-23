package com.EmployeeRating.ServiceImplementation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Dto.EmployeeRatingTrackerDto;
import com.EmployeeRating.Entity.EmployeeRatingTracker;
import com.EmployeeRating.Repository.EmployeeRatingTrackerRepo;
import com.EmployeeRating.Service.EmployeeRatingTrackerService;
@Service
public class EmployeeRatingTrackerServiceImple implements EmployeeRatingTrackerService{

	@Autowired
	EmployeeRatingTrackerRepo employeeRatingTrackerRepo;
	@Autowired
	ModelMapper mapper;
	@Override
	public ResponseEntity<?> save(EmployeeRatingTrackerDto dto) {
		EmployeeRatingTracker tracker = mapper.map(dto,EmployeeRatingTracker.class);
		EmployeeRatingTracker savedTracker = employeeRatingTrackerRepo.save(tracker);
		EmployeeRatingTrackerDto savedDto = mapper.map(savedTracker,EmployeeRatingTrackerDto.class);
		return new ResponseEntity<EmployeeRatingTrackerDto>(savedDto,HttpStatus.OK);
	}
}
