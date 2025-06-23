package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Dto.EmployeeDto;
import com.EmployeeRating.Dto.RatingDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeRatingTracker;
import com.EmployeeRating.Entity.Rating;
import com.EmployeeRating.Repository.EmployeeRatingTrackerRepo;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Repository.RatingRepo;
import com.EmployeeRating.Service.RatingService;
import com.EmployeeRating.util.AllDetails;

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
	public ResponseEntity<?> save(RatingDto dto, Long id) {
		Employee employee = employeeRepo.findById(id).get();
		if(employee==null) {
			try {
				throw new Exception();
			}
			catch (Exception e) {
				System.out.println("Employee not found");
			}
		}
			EmployeeRatingTracker tracker = new EmployeeRatingTracker();
			tracker.setIsSubmmited(true);
			tracker.setSendDate(AllDetails.date);
			tracker.setSubmitDate(LocalDate.now());
			
			Rating rating = mapper.map(dto, Rating.class);
			Long total_rating = dto.getCommunication() + dto.getLeadership() + dto.getProblem_solving()
					+ dto.getPunctuality() + dto.getTeamwork();
			rating.setAverageRating(total_rating/5f);
			employee.setRating(rating);
			employee.setEmployeeRatingTracker(tracker);
			tracker.setEmployee(employee);
			Employee savedEmployee = employeeRepo.save(employee);
			EmployeeDto savedEmployeeDto = mapper.map(savedEmployee,EmployeeDto.class);
			return new ResponseEntity<EmployeeDto>(savedEmployeeDto, HttpStatus.OK);
		
		
		
	}

	@Override
	public Rating getRating(Long id) {
		Rating rated = ratingRepo.findById(id).orElseThrow();
		return rated;
	}
}
