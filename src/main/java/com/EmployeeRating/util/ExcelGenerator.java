package com.EmployeeRating.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.Rating;

public class ExcelGenerator {
	public static byte[] generateExcelInMemory(Employee employee) throws IOException {
		Rating rating = employee.getRating();
		System.out.println("Is i am executing?");
		System.out.println("==== Inside generateExcelInMemory ====");
		System.out.println("Rating: " + employee.getRating());

		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Rating");
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Name");
		header.createCell(1).setCellValue("Punctuality");
		header.createCell(2).setCellValue("Task_allocation");
		header.createCell(3).setCellValue("Teamwork");
		header.createCell(4).setCellValue("Adaptability");
		header.createCell(5).setCellValue("quantity_and_quality");
		header.createCell(6).setCellValue("Communication");
		header.createCell(7).setCellValue("Average");
		header.createCell(8).setCellValue("Edit");

		Row row = sheet.createRow(1);
		row.createCell(0).setCellValue(employee.getEmployeeName());
		row.createCell(1).setCellValue(rating.getPunctuality());
		row.createCell(2).setCellValue(rating.getTask_allocation());
		row.createCell(3).setCellValue(rating.getTeamwork());
		row.createCell(4).setCellValue(rating.getAdaptability());
		row.createCell(5).setCellValue(rating.getQuantity_and_quality());
		row.createCell(6).setCellValue(rating.getCommunication());
		row.createCell(7).setCellValue(rating.getAverageRating());

		CreationHelper helper = workbook.getCreationHelper();
		Hyperlink link = helper.createHyperlink(HyperlinkType.URL);
		link.setAddress("http://localhost:4200/?id=" + employee.getEmployeeId());
		Cell editCell = row.createCell(8);
		editCell.setCellValue("Edit Rating");
		editCell.setHyperlink(link);

		// 6. Style the hyperlink cell (blue and underlined)
		CellStyle linkStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setUnderline(Font.U_SINGLE);
		font.setColor(IndexedColors.BLUE.getIndex());
		linkStyle.setFont(font);
		editCell.setCellStyle(linkStyle);

		// 7. Auto-size all 4 columns
		for (int i = 0; i < 9; i++) {
			sheet.autoSizeColumn(i);
		}

		// 8. Write workbook to ByteArrayOutputStream (in memory)
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();

		// 9. Return Excel file as byte array
		return bos.toByteArray();

	}

	public static byte[] generateExcelForEmployee(Employee employee) throws IOException {
		// now we have to load the excel file and edit
		Rating rating = employee.getRating();
		ClassPathResource template = new ClassPathResource("static/final.xlsm");
		Workbook workbook = WorkbookFactory.create(template.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.createRow(1);

		row.createCell(0).setCellValue(employee.getEmployeeName());
		row.createCell(1).setCellValue(employee.getEmployeeId());
		row.createCell(2).setCellValue(rating.getPunctuality());
		row.createCell(3).setCellValue(rating.getTask_allocation());
		row.createCell(4).setCellValue(rating.getTeamwork());
		row.createCell(5).setCellValue(rating.getAdaptability());
		row.createCell(6).setCellValue(rating.getCommunication());
		row.createCell(7).setCellValue(rating.getQuantity_and_quality());

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		System.out.println("Template exists: " + template.exists());
		return bos.toByteArray();

	}

	public static byte[] generateSimpleExcel() throws IOException {
		Workbook workbook = new XSSFWorkbook(); // .xlsx format
		Sheet sheet = workbook.createSheet("Employees");

		// Create header row
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Name");
		header.createCell(1).setCellValue("Email");
		header.createCell(2).setCellValue("Department");

		// Create some dummy rows
		Row row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("Amaresh Parida");
		row1.createCell(1).setCellValue("amaresh@example.com");
		row1.createCell(2).setCellValue("Backend");

		Row row2 = sheet.createRow(2);
		row2.createCell(0).setCellValue("Rohit Sharma");
		row2.createCell(1).setCellValue("rohit@example.com");
		row2.createCell(2).setCellValue("Frontend");

		// Auto size columns
		for (int i = 0; i < 3; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write to byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		return bos.toByteArray();
	}

	public static byte[] generateForProjectManager(List<Employee> employees)
			throws IOException, InvalidFormatException {
		ClassPathResource resource = new ClassPathResource("static/final.xlsm"); // ✅ no "static/" here
		OPCPackage pkg = OPCPackage.open(resource.getFile()); // ✅ must use getFile(), not getInputStream
		XSSFWorkbook workbook = new XSSFWorkbook(pkg); // ✅ macro-friendly workbook

		Sheet sheet = workbook.getSheetAt(0);

		// Set header row (row 0) with 'Average' column if not already set
		Row header = sheet.getRow(0);
		if (header == null) {
			header = sheet.createRow(0);
		}
		header.createCell(0).setCellValue("Name");
		header.createCell(1).setCellValue("Employee ID");
		header.createCell(2).setCellValue("Punctuality");
		header.createCell(3).setCellValue("Task Allocation");
		header.createCell(4).setCellValue("Teamwork");
		header.createCell(5).setCellValue("Adaptability");
		header.createCell(6).setCellValue("Communication");
		header.createCell(7).setCellValue("Quantity & Quality");
		header.createCell(8).setCellValue("Average");
		header.createCell(9).setCellValue("Score");

		for (int i = 0; i < employees.size(); i++) {
			Employee e = employees.get(i);
			Rating rating = e.getRating();

			Row row = sheet.createRow(i + 1);

			row.createCell(0).setCellValue(e.getEmployeeName());
			row.createCell(1).setCellValue(e.getEmployeeId());
			if (rating != null) {
				row.createCell(2).setCellValue(rating.getPunctuality() != null ? rating.getPunctuality() : 0);
				row.createCell(3).setCellValue(rating.getTask_allocation() != null ? rating.getTask_allocation() : 0);
				row.createCell(4).setCellValue(rating.getTeamwork() != null ? rating.getTeamwork() : 0);
				row.createCell(5).setCellValue(rating.getAdaptability() != null ? rating.getAdaptability() : 0);
				row.createCell(6).setCellValue(rating.getCommunication() != null ? rating.getCommunication() : 0);
				row.createCell(7).setCellValue(rating.getQuantity_and_quality() != null ? rating.getQuantity_and_quality() : 0);
				// Add formula for average (columns C to H, i.e., 2 to 7)
				String avgFormula = String.format("AVERAGE(C%d:H%d)", i + 2, i + 2);
				row.createCell(8).setCellFormula(avgFormula);
				// Add formula for score (column J, i.e., 9): Custom scoring based on average rating
				// 1-2 rating → 0% score, 3 rating → 50% score, 4-5 rating → 100% score
				String scoreFormula = String.format("IF(I%d<=2,0,IF(I%d=3,50,100))", i + 2, i + 2);
				row.createCell(9).setCellFormula(scoreFormula);
			} else {
				for (int j = 2; j <= 7; j++) {
					row.createCell(j).setCellValue(0);
				}
				row.createCell(8).setCellValue(0);
				row.createCell(9).setCellValue(0);
			}
		}
		// Conditional formatting for Score column (J)
		org.apache.poi.ss.usermodel.SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
		// Red: 0% (ratings 1-2)
		org.apache.poi.ss.usermodel.ConditionalFormattingRule ruleRed = sheetCF.createConditionalFormattingRule(org.apache.poi.ss.usermodel.ComparisonOperator.EQUAL, "0");
		org.apache.poi.ss.usermodel.PatternFormatting fillRed = ruleRed.createPatternFormatting();
		fillRed.setFillBackgroundColor(IndexedColors.RED.getIndex());
		fillRed.setFillPattern(org.apache.poi.ss.usermodel.PatternFormatting.SOLID_FOREGROUND);
		// Yellow: 50% (rating 3)
		org.apache.poi.ss.usermodel.ConditionalFormattingRule ruleYellow = sheetCF.createConditionalFormattingRule(org.apache.poi.ss.usermodel.ComparisonOperator.EQUAL, "50");
		org.apache.poi.ss.usermodel.PatternFormatting fillYellow = ruleYellow.createPatternFormatting();
		fillYellow.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
		fillYellow.setFillPattern(org.apache.poi.ss.usermodel.PatternFormatting.SOLID_FOREGROUND);
		// Green: 100% (ratings 4-5)
		org.apache.poi.ss.usermodel.ConditionalFormattingRule ruleGreen = sheetCF.createConditionalFormattingRule(org.apache.poi.ss.usermodel.ComparisonOperator.EQUAL, "100");
		org.apache.poi.ss.usermodel.PatternFormatting fillGreen = ruleGreen.createPatternFormatting();
		fillGreen.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
		fillGreen.setFillPattern(org.apache.poi.ss.usermodel.PatternFormatting.SOLID_FOREGROUND);
		// Apply to Score column (J2:J{lastRow})
		int lastRow = employees.size() + 1;
		org.apache.poi.ss.util.CellRangeAddress[] regions = { new org.apache.poi.ss.util.CellRangeAddress(1, lastRow, 9, 9) };
		sheetCF.addConditionalFormatting(regions, ruleRed);
		sheetCF.addConditionalFormatting(regions, ruleYellow);
		sheetCF.addConditionalFormatting(regions, ruleGreen);
		// Auto-size columns
		for (int i = 0; i <= 9; i++) {
			sheet.autoSizeColumn(i);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos); // ✅ this keeps the macro intact
		workbook.close();
		pkg.close(); // ✅ close the package too

		return bos.toByteArray();

	}

	public static byte[] generateReadOnly(List<Employee> employees) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Ratings");
		Row header = sheet.createRow(0);
		String[] column = { "Name", "Employee ID", "Punctuality", "Task Allocation", "Teamwork", "Adaptability",
				"Communication", "Quantity & Quality", "Average" };
		for (int i = 0; i < column.length; i++) {
			header.createCell(i).setCellValue(column[i]);
		}
		int rowIdx = 1;
		for (Employee e : employees) {
			Rating r = e.getRating();
			if (r == null)
				continue;
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(e.getEmployeeName());
			row.createCell(1).setCellValue(e.getEmployeeId());
			row.createCell(2).setCellValue(r.getPunctuality());
			row.createCell(3).setCellValue(r.getTask_allocation());
			row.createCell(4).setCellValue(r.getTeamwork());
			row.createCell(5).setCellValue(r.getAdaptability());
			row.createCell(6).setCellValue(r.getCommunication());
			row.createCell(7).setCellValue(r.getQuantity_and_quality());
			row.createCell(8).setCellValue(r.getAverageRating());
		}
		for (int i = 0; i < column.length; i++) {
			sheet.autoSizeColumn(i);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		return bos.toByteArray();
	}

}
