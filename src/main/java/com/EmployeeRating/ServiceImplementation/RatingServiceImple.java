package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		if (optionalemployee.isEmpty())
			return new ResponseEntity<String>("Employee not found", HttpStatus.NOT_FOUND);

		Employee employee = optionalemployee.get();
		EmployeeRatingTracker tracker = employee.getEmployeeRatingTracker();
		tracker.setTlSubmitDate(LocalDate.now());
		employee.setTLSubmitted(true);
		Rating rating = new Rating();
		rating.setAdaptability(dto.getAdaptability());
		rating.setCommunication(dto.getCommunication());
		rating.setPunctuality(dto.getPunctuality());
		rating.setQuantity_and_quality(dto.getQuantity_and_quality());
		rating.setTeamwork(dto.getTeamwork());
		rating.setTask_allocation(dto.getTask_allocation());
		Long total_rating = dto.getCommunication() + dto.getAdaptability() + dto.getQuantity_and_quality()
				+ dto.getPunctuality() + dto.getTeamwork() + dto.getTask_allocation();
		rating.setAverageRating(total_rating / 6f);
		employee.setRating(rating);
		employee.setEmployeeRatingTracker(tracker);
		tracker.setEmployee(employee);

		Employee savedEmployee = employeeRepo.save(employee);
		RatingDto savedDto = mapper.map(savedEmployee.getRating(), RatingDto.class);
		return new ResponseEntity<RatingDto>(savedDto, HttpStatus.OK);
	}

	@Override
	public Rating getRating(Long id) {
		Rating rated = ratingRepo.findById(id).orElseThrow();
		return rated;
	}

	@Override
	public ResponseEntity<?> update(RatingDto dto, String employeeId) {
		Optional<Employee> employee = employeeRepo.findByEmployeeId(employeeId);
		Employee savedEmployee = employee.get();
		if (savedEmployee != null) {
			savedEmployee.setPmSubmitted(true);
			savedEmployee.getEmployeeRatingTracker().setPmSubmitDate(LocalDate.now());
			Rating rating = savedEmployee.getRating();
			if (rating == null) {
				rating = new Rating();
			}
			rating.setPunctuality(dto.getPunctuality());
			rating.setTask_allocation(dto.getTask_allocation());
			rating.setTeamwork(dto.getTeamwork());
			rating.setAdaptability(dto.getAdaptability());
			rating.setCommunication(dto.getCommunication());
			rating.setQuantity_and_quality(dto.getQuantity_and_quality());
			rating.setAverageRating(dto.getAverageRating());
			savedEmployee.setRating(rating);
			employeeRepo.save(savedEmployee);
			return ResponseEntity.ok("Employee " + savedEmployee.getEmployeeName() + " updated successfully");
		}
		return ResponseEntity.ok("Employee id is not found");
	}

	@Override
	public ResponseEntity<?> update(List<RatingDto> dtoList) {
		
		dtoList.forEach(dto -> {
			Optional<Employee> employeePresent = employeeRepo.findByEmployeeId(dto.getEmployeeId());
			if (employeePresent.isPresent()) {
				Employee employee = employeePresent.get();
				Rating rating = employee.getRating();
				if(rating==null) {
					rating = new Rating();
					rating.setEmployee(employee);
					employee.setRating(rating);
				}
				rating.setPunctuality(dto.getPunctuality());
				rating.setTask_allocation(dto.getTask_allocation());
				rating.setTeamwork(dto.getTeamwork());
				rating.setAdaptability(dto.getAdaptability());
				rating.setCommunication(dto.getCommunication());
				rating.setQuantity_and_quality(dto.getQuantity_and_quality());
				rating.setAverageRating(dto.getAverageRating());

				rating.setEmployee(employee); // bidirectional linking
				employee.setRating(rating);

				employeeRepo.save(employee);
			}

		});
		return ResponseEntity.ok("Update successfully");
	}

    @Override
    public ResponseEntity<?> getRatingsByEmployeeIds(List<String> employeeIds) {
        List<RatingDto> ratings = new ArrayList<>();
        List<String> notFound = new ArrayList<>();
        for (String empId : employeeIds) {
            Optional<Employee> optionalEmployee = employeeRepo.findByEmployeeId(empId);
            if (optionalEmployee.isPresent() && optionalEmployee.get().getRating() != null) {
                RatingDto dto = mapper.map(optionalEmployee.get().getRating(), RatingDto.class);
                dto.setEmployeeId(empId);
                ratings.add(dto);
            } else {
                notFound.add(empId);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("ratings", ratings);
        result.put("notFound", notFound);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> bulkSaveRatings(List<RatingDto> dtos) {
        List<String> saved = new ArrayList<>();
        Map<String, String> errors = new HashMap<>();
        for (RatingDto dto : dtos) {
            if (dto.getEmployeeId() == null) {
                errors.put("unknown", "Missing employeeId");
                continue;
            }
            Optional<Employee> optionalEmployee = employeeRepo.findByEmployeeId(dto.getEmployeeId());
            if (optionalEmployee.isEmpty()) {
                errors.put(dto.getEmployeeId(), "Employee not found");
                continue;
            }
            Employee employee = optionalEmployee.get();
            EmployeeRatingTracker tracker = employee.getEmployeeRatingTracker();
            if (tracker == null) {
                tracker = new EmployeeRatingTracker();
                tracker.setEmployee(employee);
                employee.setEmployeeRatingTracker(tracker);
            }
            tracker.setTlSubmitDate(LocalDate.now());
            employee.setTLSubmitted(true);
            // Update or create rating
            Rating rating = employee.getRating();
            if (rating == null) {
                rating = new Rating();
                rating.setEmployee(employee);
                employee.setRating(rating);
            }
            rating.setPunctuality(dto.getPunctuality());
            rating.setTask_allocation(dto.getTask_allocation());
            rating.setTeamwork(dto.getTeamwork());
            rating.setAdaptability(dto.getAdaptability());
            rating.setCommunication(dto.getCommunication());
            rating.setQuantity_and_quality(dto.getQuantity_and_quality());
            Long total_rating = dto.getCommunication() + dto.getAdaptability() + dto.getQuantity_and_quality()
                + dto.getPunctuality() + dto.getTeamwork() + dto.getTask_allocation();
            rating.setAverageRating(total_rating / 6f);
            rating.setEmployee(employee); // Ensure bidirectional link
            employee.setRating(rating);
            employee.setEmployeeRatingTracker(tracker);
            tracker.setEmployee(employee);
            employeeRepo.save(employee);
            saved.add(dto.getEmployeeId());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("saved", saved);
        result.put("errors", errors);
        return ResponseEntity.ok(result);
	}
}
