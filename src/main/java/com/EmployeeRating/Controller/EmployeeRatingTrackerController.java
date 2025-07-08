package com.EmployeeRating.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeRating.Dto.EmployeeRatingTrackerDto;
import com.EmployeeRating.Service.EmployeeRatingTrackerService;

@RestController
@RequestMapping("/tracking")
public class EmployeeRatingTrackerController {
	@Autowired
	EmployeeRatingTrackerService employeeRatingTrackerService;

	@PostMapping("/save")
	public ResponseEntity<?> saveTracking(@RequestBody EmployeeRatingTrackerDto dto) {
		return employeeRatingTrackerService.save(dto);
	}

	// This method called teamlead submit that form
	@PostMapping("/tlsubmit")
	public ResponseEntity<?> tlSubmit(@RequestParam(name = "employeeId") String employeeId) {
		employeeRatingTrackerService.tlSubmit(employeeId);
		return new ResponseEntity<String>("TL Submitted successfully", HttpStatus.OK);
	}

	// This method called project manager submit that form
	@PostMapping("/pmsubmit")
	public ResponseEntity<?> pmSubmit(@RequestParam(name = "employeeId") String employeeId) {
		employeeRatingTrackerService.pmSubmit(employeeId);
		return new ResponseEntity<String>("TL Submitted successfully", HttpStatus.OK);
	}

	// This method called project manager officer submit that form
	@PostMapping("/pmosubmit")
	public ResponseEntity<?> pmoSubmit(@RequestParam(name = "employeeId") String employeeId) {
		employeeRatingTrackerService.pmoSubmit(employeeId);
		return new ResponseEntity<String>("TL Submitted successfully", HttpStatus.OK);
	}
}
