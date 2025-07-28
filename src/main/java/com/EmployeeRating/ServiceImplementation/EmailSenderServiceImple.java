package com.EmployeeRating.ServiceImplementation;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Model.FileAttachmentModel;
import com.EmployeeRating.Service.EmailSenderService;

@Service
public class EmailSenderServiceImple implements EmailSenderService {

	@Autowired
	JavaMailSender mailSender;

	@Override
	public void sendEmail(FileAttachmentModel model) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("amareshparida20@gmail.com");
		message.setTo(model.getToEmail());
		message.setSubject(model.getSubject());
		message.setText(model.getBody());
		mailSender.send(message);
	}

	@Override
	public void sendEmailWithAttachmentToTl(FileAttachmentModel model) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("amareshparida20@gmail.com");
			helper.setTo(model.getToEmail());
			helper.setSubject(model.getSubject());
			helper.setText(model.getBody(), true);
			ClassPathResource logo = new ClassPathResource("static/Logo.png"); // or "static/logo.png"
			helper.addInline("logoImage", logo); // ID must match cid:logoImage
			if (model.getAttachments() != null) {
				ByteArrayDataSource dataSource = new ByteArrayDataSource(model.getAttachments(), "application/pdf");
				helper.addAttachment("Rating.pdf", dataSource);
			}

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendEmailWithAttachementToPm(FileAttachmentModel model) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(model.getToEmail());
			helper.setSubject(model.getSubject());
			helper.setText(model.getBody(), true);
			ClassPathResource logo = new ClassPathResource("static/Logo.png"); // or "static/logo.png"
			helper.addInline("logoImage", logo); // ID must match cid:logoImage
			if (model.getAttachments() != null) {
				ByteArrayDataSource dataSource = new ByteArrayDataSource(model.getAttachments(), "application/pdf");
				helper.addAttachment("Rating.pdf", dataSource);
			}

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
