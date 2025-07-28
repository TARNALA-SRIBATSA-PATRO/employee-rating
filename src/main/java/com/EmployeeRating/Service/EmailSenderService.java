package com.EmployeeRating.Service;

import org.springframework.stereotype.Service;

import com.EmployeeRating.Model.FileAttachmentModel;
@Service
public interface EmailSenderService {

	void sendEmail(FileAttachmentModel model);
	void sendEmailWithAttachmentToTl(FileAttachmentModel model);
	void sendEmailWithAttachementToPm(FileAttachmentModel model);
}
