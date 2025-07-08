package com.EmployeeRating.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileAttachmentModel {
	private String toEmail;
	private String body;
	private String subject;
	private byte[] attachments;
	public FileAttachmentModel(String toEmail, String body, String subject) {
        this.toEmail = toEmail;
        this.body = body;
        this.subject = subject;
        this.attachments=null;
    }
}
