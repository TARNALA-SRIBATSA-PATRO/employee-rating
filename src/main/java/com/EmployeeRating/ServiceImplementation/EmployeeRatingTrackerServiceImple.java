package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Dto.EmployeeRatingTrackerDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeRatingTracker;
import com.EmployeeRating.Repository.EmployeeRatingTrackerRepo;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Service.EmployeeRatingTrackerService;
@Service
public class EmployeeRatingTrackerServiceImple implements EmployeeRatingTrackerService{

	@Autowired
	EmployeeRatingTrackerRepo employeeRatingTrackerRepo;
	@Autowired
	EmployeeRepo employeeRepo;
	@Autowired
	ModelMapper mapper;
	@Override
	public ResponseEntity<?> save(EmployeeRatingTrackerDto dto) {
		EmployeeRatingTracker tracker = mapper.map(dto,EmployeeRatingTracker.class);
		EmployeeRatingTracker savedTracker = employeeRatingTrackerRepo.save(tracker);
		EmployeeRatingTrackerDto savedDto = mapper.map(savedTracker,EmployeeRatingTrackerDto.class);
		return new ResponseEntity<EmployeeRatingTrackerDto>(savedDto,HttpStatus.OK);
	}
	@Override
	public void tlSubmit(String employeeId) {
		Optional<Employee> employee = employeeRepo.findByEmployeeId(employeeId);
		Employee savedEmployee = employee.get();
		savedEmployee.setTLSubmitted(true);
		savedEmployee.getEmployeeRatingTracker().setTlSubmitDate(LocalDate.now());
		employeeRepo.save(savedEmployee);
	}
	@Override
	public void pmSubmit(String employeeId) {
		Optional<Employee> employee = employeeRepo.findByEmployeeId(employeeId);
		Employee savedEmployee = employee.get();
		savedEmployee.setPmSubmitted(true);
		savedEmployee.getEmployeeRatingTracker().setPmSubmitDate(LocalDate.now());
		employeeRepo.save(savedEmployee);
	}
	@Override
	public void pmoSubmit(String employeeId) {
		Optional<Employee> employee = employeeRepo.findByEmployeeId(employeeId);
		Employee savedEmployee = employee.get();
		savedEmployee.setPmoSubmitted(true);
		savedEmployee.getEmployeeRatingTracker().setPmoSubmitDate(LocalDate.now());
		employeeRepo.save(savedEmployee);
	}
}
