package com.EmployeeRating.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeRating.Dto.FormData;
import com.EmployeeRating.Model.FileAttachmentModel;
import com.EmployeeRating.Service.EmailSchedulerService;
import com.EmployeeRating.Service.EmailSenderService;
import com.EmployeeRating.Service.EmployeeService;
import com.EmployeeRating.util.ExcelGenerator;

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

//	@PostMapping("/save")
//	public ResponseEntity<?> saveEmployee(@RequestBody List<EmployeeDto> employeeDto) {
//		return employeeService.save(employeeDto);
//	}
	@PostMapping("/save")
	public ResponseEntity<?> saveEmployees(@RequestBody FormData formData ) {
		System.out.println("Hey i am prinbting");
		return employeeService.save(formData);
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
		emailSenderService.sendEmailWithAttachmentToTl(model);
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
	public String getMethodText(@RequestParam(name = "param") String param) {
		return "hii";
	}

	@DeleteMapping("/delete/{empid}")
	public ResponseEntity<?> delete(@PathVariable(name = "empid") String empid) {
		return employeeService.deleteDetails(empid);
	}

	@PostMapping("/sendpdf")
	public void sendingTestMail() {
		scheduleService.sendRatingPdfToHr();
	}

	@PostMapping("/")
	public String sendingTemplate() {
		return "emailTemplate";
	}
	
	@GetMapping("/employee")
	public ResponseEntity<byte[]> dexcelSendToProjectManager(@RequestParam(name="manager")String manager) throws InvalidFormatException{
		byte[] excelData = employeeService.generateEmployeesExcel(manager);

	    HttpHeaders headers = new HttpHeaders();

	    // ✅ Use Excel MIME type for .xlsx files
	    headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel.sheet.macroEnabled.12"));

	    // ✅ Set proper filename
	    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_rating.xlsm");

	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(excelData);
	}

	@GetMapping("/employees")
	public ResponseEntity<byte[]> excelSendToProjectManagerOfficer(@RequestParam(name="managerOfficer")String managerOfficer) throws InvalidFormatException{
		byte[] excelData = employeeService.generateEmployeesExcelForManagerOfficer(managerOfficer);

	    HttpHeaders headers = new HttpHeaders();

	    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
	    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_rating_pmo.xlsx");

	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(excelData);
	}
	@GetMapping("/employeesHr")
	public ResponseEntity<byte[]> excelSendToHr() throws InvalidFormatException{
		byte[] excelData = employeeService.generateEmployeesExcelHr();

	    HttpHeaders headers = new HttpHeaders();

	    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
	    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_rating_hr.xlsx");

	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(excelData);
	}

	@GetMapping("/employee/{id}")
	public ResponseEntity<byte[]> downloadExcel(@PathVariable Long id) {
	    byte[] excelData = employeeService.generateEmployeeExcel(id);

	    HttpHeaders headers = new HttpHeaders();

	    // ✅ Use Excel MIME type for .xlsx files
	    headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel.sheet.macroEnabled.12"));

	    // ✅ Set proper filename
	    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_rating.xlsm");

	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(excelData);
	}
	
	
	@GetMapping("/simple-excel")
	public ResponseEntity<byte[]> downloadSimpleExcel() throws IOException {
	    byte[] data = ExcelGenerator.generateSimpleExcel();

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
	    headers.setContentDispositionFormData("attachment", "simple_employee_list.xlsx");
	    headers.setContentLength(data.length);

	    return ResponseEntity.ok().headers(headers).body(data);
	}

    @GetMapping("/allemployeeids")
    public ResponseEntity<List<String>> getAllEmployeeIds() {
        List<String> ids = employeeService.getAllEmployeeIds();
        return ResponseEntity.ok(ids);
	}
}
