package com.EmployeeRating.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeRating.Dto.EmployeeDto;
import com.EmployeeRating.Model.FileAttachmentModel;
import com.EmployeeRating.Service.EmailSchedulerService;
import com.EmployeeRating.Service.EmailSenderService;
import com.EmployeeRating.Service.EmployeeService;

//@CrossOrigin(origins = "http://localhost:56548")
@RestController
@RequestMapping("/api")
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;
	@Autowired
	EmailSenderService emailSenderService;
	@Autowired
	EmailSchedulerService scheduleService; 
	@PostMapping("/save")
	public ResponseEntity<?> saveEmployee(@RequestBody List<EmployeeDto> employeeDto) {
		return employeeService.save(employeeDto);
	}
	
	@GetMapping("/send")
	public String sendEmail(@RequestParam(name = "toEmail") String toEmail,
			@RequestParam(name = "subject") String subject, @RequestParam(name = "body") String body) {
		FileAttachmentModel model = new FileAttachmentModel(toEmail, body, subject);
		emailSenderService.sendEmail(model);
		return "Mail send successfully";
	}

	@PostMapping("/sendFile")
	public String sendEmailWithImageAndFile(@RequestBody FileAttachmentModel model) {
		emailSenderService.sendEmailWithAttachment(model);
		return "email sent successfully to " + model.getToEmail();
	}

	@GetMapping("/fetchAll")
	public ResponseEntity<?> fetchAll() {
		return employeeService.fetchAll();
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getEmployeeWithSpecificData(
			@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return employeeService.getEmployee(date);
	}

	@GetMapping("/getByCriteria")
	public ResponseEntity<?> getEmployeeWithCriteriaManagerEmail(
			@RequestParam(name = "managerEmail") String managerEmail) {
		return employeeService.getByCriteria(managerEmail);
	}
	@GetMapping("/getEmail")
	public String getMethodName(@RequestParam String param) {
		return new String();
	}
	@GetMapping("/getText")
	public String getMethodText(@RequestParam(name="param") String param) {
		return "hii";
	}
	@DeleteMapping("/delete/{empid}")
	public ResponseEntity<?> delete(@PathVariable(name="empid") String empid){
		return employeeService.deleteDetails(empid);
	}
	@PostMapping("/")
	public void sendingTestMail() {
		scheduleService.sendRatingPdfToHr();
	}
	
}
