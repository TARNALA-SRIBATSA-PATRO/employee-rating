package com.EmployeeRating.Service;

import com.EmployeeRating.Model.FileAttachmentModel;

public interface EmailSenderService {

	void sendEmail(String toEmail, String subject,String body);
	void sendEmailWithAttachment(FileAttachmentModel model);
}
