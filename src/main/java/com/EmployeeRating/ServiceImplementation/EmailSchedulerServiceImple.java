package com.EmployeeRating.ServiceImplementation;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

	public String getHtmlTemplate(String filename) throws Exception {
		ClassPathResource resource = new ClassPathResource("static/" + filename);
		return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
	}
	@Scheduled(cron = "0 0 11 25 * ?")
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
			if ((((today.getDayOfMonth() == 25) && (employee.getEmployeeRatingTracker().getSendDateToTL() == null))
					|| ((employee.getEmployeeRatingTracker().getSendDateToTL() == null)
							&& employee.getEndDate() != null))
					&& !

					(employee.isNoticePeriod() || employee.isProbationaPeriod())) {
				{
					model.setToEmail(employee.getTeamLeadEmail());
					model.setSubject("Employee rating for " + employee.getEmployeeName());

					try {
						String htmlContent = getHtmlTemplate("email-template(rating page).html");
						// Replace the button link in the template
						htmlContent = htmlContent.replace("<a href=\"#\" class=\"btn\">Please Give the Employee Ratings</a>",
								"<a href=\"https://employee-rating-app-t2cr.vercel.app/employee?empId=" + employee.getId() + "\" class=\"btn\">Please Give the Employee Ratings</a>");
						htmlContent = htmlContent.replace("${name}", employee.getEmployeeName().toUpperCase());
						model.setBody(htmlContent);
						emailSenderService.sendEmailWithAttachmentToTl(model);
						EmployeeRatingTracker tracker = employee.getEmployeeRatingTracker();
						tracker.setSendDateToTL(LocalDate.now());
						employee.setEmployeeRatingTracker(tracker);
						employeeRepo.save(employee);
						System.out.println("Mail send successfully");
					} catch (Exception e) {
						e.printStackTrace();
					}
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

			String body = "Dear Employee,<br><br>" + "Please find the performance rating of employee <b>"
					+ employee.getEmployeeName() + "</b>:<br><br>"

					+ "  Criteria                Rating (1-5)" + " Punctuality              " + rating.getPunctuality()
					+ "</td></tr>" + "Problem Solving           " + rating.getTask_allocation() + "</td></tr>"
					+ "   Teamwork               " + rating.getTeamwork() + "</td></tr>" + "  Leadership              "
					+ rating.getQuantity_and_quality() + "</td></tr>" + " Communication            "
					+ rating.getCommunication() + "</td></tr>" + "  Average                   "
					+ rating.getAverageRating() + "</b></td></tr>" + "Thank you.<br><br>" + "Employee Rating System";
			model.setBody(body);
			emailSenderService.sendEmail(model);
		}
	}

	@Override
	public void sendEmailDataToEmployeeb() {

	}

	@Scheduled(cron = "0 0 11 25 * ?")
	@Override
	public void SendEmailToProjectManager() {
		FileAttachmentModel model = new FileAttachmentModel();
		Map<String, List<Employee>> groupByPmEmail = employeeRepo.findAll().stream()
				.collect(Collectors.groupingBy(Employee::getProjectManagerEmail));
		for (Map.Entry<String, List<Employee>> entry : groupByPmEmail.entrySet()) {
			String email = entry.getKey();
			List<Employee> employees = entry.getValue();
			try {
				String htmlContent = getHtmlTemplate("email-template(for pmo and hr).html");
				htmlContent = htmlContent.replace("${ratinglink}", "http://localhost:8080/api/employee?manager="
						+ entry.getValue().get(0).getProjectManagerEmail());
				htmlContent = htmlContent.replace("${name}", employees.get(0).getProjectManagerName().toUpperCase());
				model.setToEmail(email);
				model.setSubject("Employee Ratings - Monthly Report To Project Manager");
				model.setBody(htmlContent);

				emailSenderService.sendEmailWithAttachementToPm(model);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
//		List<Employee> employees = employeeRepo.findAll();
//		FileAttachmentModel model = new FileAttachmentModel();
//		for (Employee employee : employees) {
//			if (employee.isTLSubmitted() && employee.getEmployeeRatingTracker().getSendDateToPm() == null) {
//				model.setToEmail(employee.getProjectManagerEmail());
//				model.setSubject("Employee rating for " + employee.getEmployeeName());
//
//				try {
//					String htmlContent = getHtmlTemplate("emailTemplate.html");
//					htmlContent = htmlContent.replace("${ratinglink}",
//							"http://localhost:8080/api/employee/" + employee.getId());
//					htmlContent = htmlContent.replace("${name}", employee.getEmployeeName().toUpperCase());
//					model.setBody(htmlContent);
//					emailSenderService.sendEmailWithAttachmentToTl(model);
//					EmployeeRatingTracker tracker = employee.getEmployeeRatingTracker();
//					tracker.setSendDateToPm(LocalDate.now());
//					employee.setEmployeeRatingTracker(tracker);
//					employeeRepo.save(employee);
//					System.out.println("Mail send successfully");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}

	@Scheduled(cron = "0 * * * * ?")
	@Override
	public void sendEmailToPmo() {
		// FileAttachmentModel model = new FileAttachmentModel();
		// Map<String, List<Employee>> groupByPmoEmail = employeeRepo.findAll().stream()
		//         .collect(Collectors.groupingBy(Employee::getPmoEmail));
		// for (Map.Entry<String, List<Employee>> entry : groupByPmoEmail.entrySet()) {
		//     String email = entry.getKey();
		//     List<Employee> employees = entry.getValue();
		//     try {
		//         String htmlContent = getHtmlTemplate("emailTemplate.html");
		//         htmlContent = htmlContent.replace("${ratinglink}",
		//                 "http://localhost:8080/api/employees?managerOfficer=" + entry.getValue().get(0).getPmoEmail());
		//         htmlContent = htmlContent.replace("${name}", employees.get(0).getPmoName().toUpperCase());
		//         model.setToEmail(email);
		//         model.setSubject("Employee Ratings - Monthly Report To Project Manager Officer");
		//         model.setBody(htmlContent); // ✅ HTML content
		//
		//         emailSenderService.sendEmailWithAttachementToPm(model);
		//
		//     } catch (Exception e) {
		//         e.printStackTrace();
		//     }
		// }
	}

	@Scheduled(cron = "0 * * * * ?")
	@Override
	public void sendRatingPdfToHr() {
		// FileAttachmentModel model = new FileAttachmentModel();
		// try {
		//     String htmlContent = getHtmlTemplate("emailTemplate.html");
		//     htmlContent = htmlContent.replace("${ratinglink}",
		//             "http://localhost:8080/api/employeesHr");
		//     htmlContent = htmlContent.replace("${name}", "Dear Hr");
		//     model.setToEmail("amareshparida20@gmail.com");
		//     model.setSubject("Employee Ratings - Monthly Report Sending to Hr");
		//     model.setBody(htmlContent); 
		//
		//     emailSenderService.sendEmailWithAttachementToPm(model);
		//
		// } catch (Exception e) {
		//     e.printStackTrace();
		// }
	}
}