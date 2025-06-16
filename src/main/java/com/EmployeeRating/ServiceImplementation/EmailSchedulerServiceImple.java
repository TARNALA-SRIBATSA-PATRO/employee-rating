package com.EmployeeRating.ServiceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Model.FileAttachmentModel;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Service.EmailSchedulerService;
import com.EmployeeRating.Service.EmailSenderService;

@Service
public class EmailSchedulerServiceImple implements EmailSchedulerService {
	@Autowired
	EmailSenderService emailSenderService;
	@Autowired
	EmployeeRepo employeeRepo;

	@Scheduled(cron = "0 * * * * ?")
	@Override
	public void sendEmailParticular() {
		FileAttachmentModel model = new FileAttachmentModel();
		List<Employee> employees = employeeRepo.findAll();
		for (Employee employee : employees) {
			if (employee.getManagerEmail() != null) {
				model.setToEmail(employee.getManagerEmail());
				model.setSubject("Employee rating for " + employee.getName());
				model.setBody("Sir you have to give rating");
				emailSenderService.sendEmail(model.getToEmail(), model.getSubject(), model.getBody());

			}
		}

	}

}
