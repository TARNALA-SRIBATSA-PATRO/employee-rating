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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String employeeId;
	
	@Column(nullable = false)
	private String employeeName;
	
	@Column(nullable = false)
	private String employeeEmail;
	
	@Column(nullable = false)
	private String projectManager;
	
	@Column(nullable = false)	
	private String projectManagerEmail;
	
	private boolean pmSubmitted;
		
	@JsonFormat(pattern = "yyyy-MM-dd") // 👈 defines how to parse it
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
	
	private String teamLead;
	
	private String teamLeadEmail;
	
	private boolean isTLSubmitted;
	
	private boolean isHrSend;

	private String pmoName;
	
	private String pmoEmail;
	
	private boolean isPmoSubmitted;
	
	private LocalDate joiningDate;
	
	private LocalDate leaveDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="rating_id")
	@JsonIgnore
	private Rating rating;

	private boolean noticePeriod;
	
	private boolean probationaPeriod;

	@OneToOne(mappedBy = "employee",cascade = CascadeType.ALL)
	@JsonIgnore
	EmployeeRatingTracker employeeRatingTracker;
}
