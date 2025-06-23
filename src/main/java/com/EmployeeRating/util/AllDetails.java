package com.EmployeeRating.util;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AllDetails {
	public static LocalDate date =  LocalDate.now().withDayOfMonth(20);
}
