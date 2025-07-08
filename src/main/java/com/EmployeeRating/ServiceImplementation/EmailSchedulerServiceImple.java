package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

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
import com.EmployeeRating.util.PdfGenerator;

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
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
//		Root<Employee> root = cq.from(Employee.class);
//		LocalDate today = LocalDate.now();
//		Predicate combined = cb.between(cb.literal(today), root.get("startDate"), root.get("endDate"));
//		cq.select(root).where(combined);
//		TypedQuery<Employee> query = entityManager.createQuery(cq);
//		List<Employee> employees = query.getResultList();
//		for (Employee employee : employees) {
//			if (employee.getProjectManagerEmail() != null) {
//				model.setToEmail(employee.getProjectManagerEmail());
//				model.setSubject("Employee rating for " + employee.getEmployeeName());
//				String formLink = "http://localhost:4200/?empId=" + employee.getId();
//
//				String body = "Dear Sir,<br><br>" + "Please rate employee <b>" + employee.getEmployeeName()
//						+ "</b>.<br>" + "<a href=\"" + formLink + "\">Click here to open the rating form</a><br><br>"
//						+ "Thank you.";
//				model.setBody(body);
//				emailSenderService.sendEmail(model);
//			}
//		}

		List<Employee> employees = employeeRepo.findAll();
		LocalDate today = LocalDate.now();
		for (Employee employee : employees) {
			if ((((today.getDayOfMonth() == 20) && (employee.getEmployeeRatingTracker().getSendDateToTL() == null))
					|| ((employee.getEmployeeRatingTracker().getSendDateToTL() == null)
							&& employee.getEndDate() != null))
					&& !

					(employee.isNoticePeriod() || employee.isProbationaPeriod())) {
				{
					model.setToEmail(employee.getTeamLeadEmail());
					model.setSubject("Employee rating for " + employee.getEmployeeName());
					String formLink = "http://localhost:4200/?empId=" + employee.getId();

					String body = "Dear Sir,<br><br>" + "Please rate employee <b>" + employee.getEmployeeName()
							+ "</b>.<br>" + "<a href=\"" + formLink
							+ "\">Click here to open the rating form</a><br><br>" + "Thank you.";
					model.setBody(body);
					emailSenderService.sendEmail(model);
					EmployeeRatingTracker tracker = employee.getEmployeeRatingTracker();
					tracker.setSendDateToTL(LocalDate.now());
					employee.setEmployeeRatingTracker(tracker);
					employeeRepo.save(employee);
				}

			}
		}
	}

	// @Scheduled(cron = "0 * * * * ?")
	@Override
	public void sendEmailDataToEmployee() {
		List<EmployeeRatingTracker> trackers = trackerRepo.findAll();
		List<EmployeeRatingTracker> submitted = new ArrayList<EmployeeRatingTracker>();
		for (EmployeeRatingTracker t : trackers) {
			if (t.getIsSubmmited())
				submitted.add(t);
		}
		for (EmployeeRatingTracker s : submitted) {
			FileAttachmentModel model = new FileAttachmentModel();
			Employee employee = s.getEmployee();
			Rating rating = employee.getRating();

			model.setToEmail(employee.getEmployeeEmail());
			model.setSubject("Rumango rating of " + employee.getEmployeeName());

			String formLink = "http://localhost:4200/";
			String body = "Dear Employee,<br><br>" + "Please find the performance rating of employee <b>"
					+ employee.getEmployeeName() + "</b>:<br><br>"

					+ "  Criteria                Rating (1-5)" + " Punctuality              " + rating.getPunctuality()
					+ "</td></tr>" + "Problem Solving           " + rating.getTask_allocation() + "</td></tr>"
					+ "   Teamwork               " + rating.getTeamwork() + "</td></tr>" + "  Leadership              "
					+ rating.getQuantity_and_quality() + "</td></tr>" + " Communication            "
					+ rating.getCommunication() + "</td></tr>" + "  Average                   "
					+ rating.getAverageRating() + "</b></td></tr>"

					+ "Thank you.<br><br>" + "Employee Rating System";
			model.setBody(body);
			emailSenderService.sendEmail(model);
		}
	}

	@Override
	public void sendEmailDataToEmployeeb() {

	}

	@Scheduled(cron = "0 * * * * ?")
	@Override
	public void SendEmialToProjectManager() {
		List<Employee> employees = employeeRepo.findAll();

		employees.stream().forEach(employee -> {
			if (employee.isTLSubmitted() && (employee.getEmployeeRatingTracker().getSendDateToPm() == null)) {
				String formlink = "project_from.html";
				String message = "Sir please verify rating given by " + employee.getTeamLead() + " to "
						+ employee.getEmployeeName();
				FileAttachmentModel model = new FileAttachmentModel(employee.getProjectManagerEmail(), formlink,
						message, null);
				emailSenderService.sendEmail(model);
				employee.getEmployeeRatingTracker().setSendDateToPm(LocalDate.now());
				employeeRepo.save(employee);
			}
		});
	}

	@Scheduled(cron = "0 * * * * ?")
	@Override
	public void sendEmailToPmo() {
		List<Employee> employees = employeeRepo.findAll();
		employees.stream().forEach(employee -> {
			if (employee.isPmSubmitted() && (employee.getEmployeeRatingTracker().getSendDateToPmo() == null)) {
				String formlink = "project_from.html";
				String message = "Sir please verify rating given by " + employee.getProjectManager() + " to "
						+ employee.getEmployeeName();
				FileAttachmentModel model = new FileAttachmentModel(employee.getPmoEmail(), formlink, message, null);
				emailSenderService.sendEmail(model);
				employee.getEmployeeRatingTracker().setSendDateToPmo(LocalDate.now());
				employeeRepo.save(employee);
			}
		});
	}

	@Scheduled(cron = "0 * * * * ?")
	@Override
	public void sendRatingPdfToHr() {
		List<Employee> employees = employeeRepo.findAll();
		employees.stream().forEach(employee -> {
			if (employee.isPmoSubmitted() && !employee.isHrSend()) {

				String form = "project_from.html";
				byte[] attachments = PdfGenerator.generatePdf(employee);
				String message = "Dear HR please find the details of employee " + employee.getEmployeeName();
				FileAttachmentModel model = new FileAttachmentModel("amareshparida10@gmail.com", form, message,
						attachments);
				emailSenderService.sendEmailWithAttachment(model);
				employee.getEmployeeRatingTracker().setSendToHr(LocalDate.now());
				employee.setHrSend(true);
				employeeRepo.save(employee);
			}
		});
	}

}