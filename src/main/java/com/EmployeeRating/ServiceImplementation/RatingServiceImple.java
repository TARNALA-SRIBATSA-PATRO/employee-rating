package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Dto.RatingDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeRatingTracker;
import com.EmployeeRating.Entity.Rating;
import com.EmployeeRating.Repository.EmployeeRatingTrackerRepo;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Repository.RatingRepo;
import com.EmployeeRating.Service.RatingService;

@Service
public class RatingServiceImple implements RatingService {

	@Autowired
	RatingRepo ratingRepo;
	@Autowired
	EmployeeRepo employeeRepo;
	@Autowired
	ModelMapper mapper;
	@Autowired
	EmployeeRatingTrackerRepo trackerRepo;
	@Override
	public ResponseEntity<?> save(RatingDto dto, String empid) {
		Optional<Employee> optionalemployee = employeeRepo.findByEmployeeId(empid);
		if(optionalemployee.isEmpty()) 
				return new ResponseEntity<String> ("Employee not found",HttpStatus.NOT_FOUND);
			
		Employee employee = optionalemployee.get();
			EmployeeRatingTracker tracker = employee.getEmployeeRatingTracker();
			tracker.setTlSubmitDate(LocalDate.now());
			employee.setTLSubmitted(true);
			Rating rating = mapper.map(dto, Rating.class);
			Long total_rating = dto.getCommunication() + dto.getAdaptability() + dto.getQuantity_and_quality()
				+ dto.getPunctuality() + dto.getTeamwork()+dto.getTask_allocation();
			rating.setAverageRating(total_rating/6f);
			employee.setRating(rating);
			employee.setEmployeeRatingTracker(tracker);
			tracker.setEmployee(employee);
			Employee savedEmployee = employeeRepo.save(employee);
			RatingDto savedDto = mapper.map(savedEmployee.getRating(),RatingDto.class);
			return new ResponseEntity<RatingDto>(savedDto, HttpStatus.OK);	
	}

	@Override
	public Rating getRating(Long id) {
		Rating rated = ratingRepo.findById(id).orElseThrow();
		return rated;
	}
}
