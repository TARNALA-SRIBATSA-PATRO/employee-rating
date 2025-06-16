package com.EmployeeRating.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeRating.Dto.EmployeeDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Model.FileAttachmentModel;
import com.EmployeeRating.Service.EmailSenderService;
import com.EmployeeRating.Service.EmployeeService;


//@CrossOrigin(origins = "http://localhost:56548")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;
	@Autowired
	ModelMapper mapper;
	@Autowired
	EmailSenderService emailSenderService;
	@PostMapping("/save")
	public ResponseEntity<?> saveEmployee(@RequestBody EmployeeDto employeeDto) {
		Employee employee = mapper.map(employeeDto,Employee.class);
		employeeService.save(employee);
		return new ResponseEntity<String>("Successfully saved",HttpStatus.OK);
	}
	@GetMapping("/send")
	public String sendEmail(@RequestParam(name="toEmail") String toEmail,@RequestParam(name="subject")String subject,@RequestParam(name="body")String body) {
		emailSenderService.sendEmail(toEmail,subject,body);
		return "Mail send successfully";
	}
	@PostMapping("/sendFile")
	public String sendEmailWithImageAndFile(@RequestBody FileAttachmentModel model) {
		emailSenderService.sendEmailWithAttachment(model);
		return "email sent successfully to "+model.getToEmail();
	}
	
	
}
