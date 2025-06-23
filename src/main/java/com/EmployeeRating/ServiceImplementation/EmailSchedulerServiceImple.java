package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeRatingTracker;
import com.EmployeeRating.Entity.Rating;
import com.EmployeeRating.Model.FileAttachmentModel;
import com.EmployeeRating.Repository.EmployeeRatingTrackerRepo;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Service.EmailSchedulerService;
import com.EmployeeRating.Service.EmailSenderService;

@Service
public class EmailSchedulerServiceImple implements EmailSchedulerService {
	@Autowired
	EmailSenderService emailSenderService;
	@Autowired
	EmployeeRepo employeeRepo;
	@Autowired
	EntityManager entityManager;
	@Autowired
	EmployeeRatingTrackerRepo trackerRepo;
	@Scheduled(cron = "0 * * * * ?")
	@Override
	public void sendEmailParticular() {
		FileAttachmentModel model = new FileAttachmentModel();
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> root = cq.from(Employee.class);
		LocalDate today = LocalDate.now();
		Predicate combined = cb.between(cb.literal(today), root.get("startDate"), root.get("endDate"));
		cq.select(root).where(combined);
		TypedQuery<Employee> query = entityManager.createQuery(cq);
		List<Employee> employees = query.getResultList();		
		for (Employee employee : employees) {
			if (employee.getProjectManagerEmail() != null) {
				model.setToEmail(employee.getProjectManagerEmail());
				model.setSubject("Employee rating for " + employee.getName());
				String formLink = "http://localhost:8080/rating_form.html?empId=" + employee.getId();

				String body = "Dear Sir,<br><br>"
				           + "Please rate employee <b>" + employee.getName() + "</b>.<br>"
				           + "<a href=\"" + formLink + "\">Click here to open the rating form</a><br><br>"
				           + "Thank you.";
				model.setBody(body);
				emailSenderService.sendEmail(model.getToEmail(), model.getSubject(), model.getBody());
			}
		}
	}
	@Scheduled(cron = "0 * * * * ?")
	@Override
	public void sendEmailDataToEmployee() {
		List<EmployeeRatingTracker> trackers = trackerRepo.findAll();
		List<EmployeeRatingTracker> submitted = new ArrayList<EmployeeRatingTracker>();
		for(EmployeeRatingTracker t : trackers) {
			if(t.getIsSubmmited())submitted.add(t);
		}
		for(EmployeeRatingTracker s : submitted) {
			FileAttachmentModel model = new FileAttachmentModel();
			Employee employee = s.getEmployee();
			Rating rating = employee.getRating();
			
			model.setToEmail(employee.getEmail());
			model.setSubject("Rumango rating of "+employee.getName());
			
			String formLink = "http://localhost:8080/project_form.html";

			
			String body = "Dear Employee,<br><br>"
			           + "Please find the performance rating of employee <b>" + employee.getName() + "</b>:<br><br>"

			           + "  Criteria                Rating (1-5)"
			           + " Punctuality              " + rating.getPunctuality() + "</td></tr>"
			           + "Problem Solving           " + rating.getProblem_solving() + "</td></tr>"
			           + "   Teamwork               " + rating.getTeamwork() + "</td></tr>"
			           + "  Leadership              " + rating.getLeadership() + "</td></tr>"
			           + " Communication            " + rating.getCommunication() + "</td></tr>"
			           + "  Average                   " + rating.getAverageRating() + "</b></td></tr>"
			          
			           + "Thank you.<br><br>"
			           + "Employee Rating System";
			model.setBody(body);
			emailSenderService.sendEmail(model.getToEmail(), model.getSubject(),model.getBody());
		}
	}
}