package com.EmployeeRating.Entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRatingTracker {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate sendDateToTL;
	
	@Column(name="TeamLead_Submitted")
	private LocalDate tlSubmitDate;
	
	@Column(name="Send_Date_ProjectManager")
	private LocalDate sendDateToPm;
	
	@Column(name="ProjectManager_Submitted")
	private LocalDate pmSubmitDate;
	
	@Column(name="send_Date_ProjectManagerOffice")
	private LocalDate sendDateToPmo;
	
	@Column(name="ProjectManagerOfficer_submitted")
	private LocalDate pmoSubmitDate;
	
	@Column(name="send_To_Hr")
	private LocalDate sendToHr;
	
	private Boolean isSubmmited = false; 
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="employee_id")
	@JsonIgnore
	private Employee employee;
}
