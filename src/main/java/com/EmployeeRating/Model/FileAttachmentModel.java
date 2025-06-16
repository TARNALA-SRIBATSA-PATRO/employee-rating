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
	private String attachments;
}
