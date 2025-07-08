package com.EmployeeRating.util;

import java.io.ByteArrayOutputStream;

import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.Rating;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfGenerator {
	public static byte[] generatePdf(Employee employee) {
		Document document = new Document();
		Rating rating = employee.getRating(); 
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PdfWriter write = PdfWriter.getInstance(document,out );
		try {
			//connect the blank document to output stream
			PdfWriter.getInstance(document, out);
			
			//Oeping the document to do all these file editing
			document.open();
			
			//Adding the top as employee details
			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD,16);
			Paragraph title = new Paragraph("Employee Details",font);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			
			//Adding space
			document.add(new Paragraph(" "));
			
			//Creating table with 6 column
			PdfPTable table = new PdfPTable(7); // 6 columns
			table.setWidthPercentage(100);
			
			table.addCell("Punctuality");
			table.addCell("Task_allocation");
			table.addCell("Teamwork");
			table.addCell("Adaptability");
			table.addCell("Communication");
			table.addCell("Quality_and_Quantity");
			table.addCell("Average");

			// Now add employee data in a single row
			
			    table.addCell(String.valueOf(rating.getPunctuality()));
			    table.addCell(String.valueOf(rating.getTask_allocation()));
			    table.addCell(String.valueOf(rating.getTeamwork()));
			    table.addCell(String.valueOf(rating.getAdaptability()));
			    table.addCell(String.valueOf(rating.getCommunication()));
			    table.addCell(String.valueOf(rating.getQuantity_and_quality()));
			    table.addCell(String.valueOf(rating.getAverageRating()));

			
	         document.add(table);
	         document.close();
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
		return out.toByteArray();
	}
}
