package com.EmployeeRating.ServiceImplementation;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Model.FileAttachmentModel;
import com.EmployeeRating.Service.EmailSenderService;
@Service
public class EmailSenderServiceImple implements EmailSenderService{

	@Autowired
	JavaMailSender mailSender;
	@Override
	public void sendEmail(String toEmail, String subject,String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("amareshparida20@gmail.com");
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}
	@Override
	public void sendEmailWithAttachment(FileAttachmentModel model) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message,true);
			helper.setFrom("amareshparida20@gmail.com");
			helper.setTo(model.getToEmail());
			helper.setSubject(model.getSubject());
			helper.setText(model.getBody());
			
			 FileSystemResource file
             = new FileSystemResource(
                 new File(model.getAttachments()));

         helper.addAttachment(
             file.getFilename(), file);
         mailSender.send(message);
		}catch (Exception e) {
			
		}
	}
	

}
