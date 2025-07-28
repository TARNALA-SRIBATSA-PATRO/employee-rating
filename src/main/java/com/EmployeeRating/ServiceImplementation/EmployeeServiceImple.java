package com.EmployeeRating.ServiceImplementation;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Dto.EmployeeDto;
import com.EmployeeRating.Dto.FormData;
import com.EmployeeRating.Dto.IndividualData;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeRatingTracker;
import com.EmployeeRating.Repository.EmployeeRatingTrackerRepo;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Service.EmployeeService;
import com.EmployeeRating.util.ExcelGenerator;

@Service
public class EmployeeServiceImple implements EmployeeService {
	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	EmployeeRatingTrackerRepo employeeRatingTrackerRepo;
	@Autowired
	ModelMapper mapper;
	@Autowired
	EntityManager entityManager;


//	@Override
//	public ResponseEntity<?> save(List<EmployeeDto> dto) {
//		if (dto.isEmpty())
//			return new ResponseEntity<String>("Please fill the form", HttpStatus.NOT_ACCEPTABLE);
//		List<Employee> savingEmployee = dto.stream().map(employeeDto -> {
//			Employee e = mapper.map(employeeDto, Employee.class);
//			return e;
//		}).collect(Collectors.toList());
//		List<Employee> savedEmployee = employeeRepo.saveAll(savingEmployee);
//		List<EmployeeDto> returningDto = savedEmployee.stream().map(returnDto -> {
//			EmployeeDto d = mapper.map(returnDto, EmployeeDto.class);
//			return d;
//		}).collect(Collectors.toList());
//		return new ResponseEntity<List<EmployeeDto>>(returningDto, HttpStatus.OK);
//	}

	@Override
	public ResponseEntity<?> save(List<EmployeeDto> dto) {
		if(dto.isEmpty())return new ResponseEntity<String> ("Please fill the form",HttpStatus.NOT_ACCEPTABLE);
		List<Employee> savingEmployee = dto.stream().map(
				employeeDto->{
					Employee e = mapper.map(employeeDto,Employee.class);
					EmployeeRatingTracker tracker = new EmployeeRatingTracker();
					tracker.setEmployee(e); 
					e.setEmployeeRatingTracker(tracker);
					return e;
				}
				).collect(Collectors.toList());
		
		List<Employee> savedEmployee = employeeRepo.saveAll(savingEmployee);
		List<EmployeeDto> returningDto = savedEmployee.stream().map(
				returnDto->{
					EmployeeDto d = mapper.map(returnDto,EmployeeDto.class);
					return d;
				}
				).collect(Collectors.toList());
		return new ResponseEntity<List<EmployeeDto>>(returningDto,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> fetchAll() {
		List<Employee> employees = employeeRepo.findAll();
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getEmployee(LocalDate date) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> root = cq.from(Employee.class);
		cq.select(root).where(cb.equal(root.get("startDate"), date));
		TypedQuery<Employee> query = entityManager.createQuery(cq);
		List<Employee> result = query.getResultList();
		return new ResponseEntity<List<Employee>>(result, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getByCriteria(String managerEmail) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> root = cq.from(Employee.class);
		cq.select(root).where(cb.equal(root.get("projectManagerEmail"), managerEmail));

		TypedQuery<Employee> query = entityManager.createQuery(cq);

		List<Employee> employees = query.getResultList();
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteDetails(String empid) {
		Optional<Employee> savedEmployee = employeeRepo.findByEmployeeId(empid);
		if (savedEmployee.isEmpty())
			return new ResponseEntity<String>("No data found successfully", HttpStatus.NOT_FOUND);
		else {
			employeeRepo.deleteById(savedEmployee.get().getId());
			return new ResponseEntity<String>("Employee deleted successfully", HttpStatus.NOT_FOUND);
		}
	}
	
	
	@Override
	public byte[] generateEmployeeExcel(Long employeeId) {
        Employee employee = employeeRepo.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        try {
            return ExcelGenerator.generateExcelForEmployee(employee);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel: " + e.getMessage());
        }
    }

	@Override
	public byte[] generateEmployeesExcel(String projectManagerEmail) throws InvalidFormatException {
		List<Employee> employees = employeeRepo.findByProjectManagerEmail(projectManagerEmail);
		if(employees==null) {
			throw new RuntimeErrorException(null, "There is no employee");
		}
		else {
			try {
				return ExcelGenerator.generateForProjectManager(employees);
			} catch (IOException e) {
				throw new RuntimeErrorException(null, "Failed to generate Excel : "+e.getMessage());
			}
		}
	}


	@Override
	public ResponseEntity<?> save(FormData formData) {
	    List<Employee> savedEmployees = new ArrayList<>();

	    List<IndividualData> individualDtoList = formData.getEmployees();

	    for (IndividualData individual : individualDtoList) {
	        // Create Employee and Tracker
	        Employee employee = new Employee();
	        EmployeeRatingTracker tracker = new EmployeeRatingTracker();

	        // Set relationship (owning side only)
	        employee.setEmployeeRatingTracker(tracker);
	        tracker.setEmployee(employee); 

	        // Set individual fields
	        employee.setEmployeeId(individual.getEmployeeId());
	        employee.setEmployeeName(individual.getEmployeeName());
	        employee.setEmployeeEmail(individual.getEmployeeEmail());
	        employee.setJoiningDate(individual.getJoiningDate());
	        employee.setDesignation(individual.getDesignation());

	        // Set common fields
	        employee.setProjectName(formData.getProjectName());
	        employee.setProjectManagerName(formData.getProjectManagerName());
	        employee.setProjectManagerEmail(formData.getProjectManagerEmail());
	        employee.setTeamLead(formData.getTeamLeadName());
	        employee.setTeamLeadEmail(formData.getTeamLeadEmail());
	        employee.setPmoName(formData.getPmoName());
	        employee.setPmoEmail(formData.getPmoEmail());
	        employee.setStartDate(formData.getProjectStartDate());
	        employee.setEndDate(formData.getProjectEndDate());

	        // Save only the employee (tracker will be saved via cascade)
	        savedEmployees.add(employeeRepo.save(employee));
	    }

	    return ResponseEntity.ok(savedEmployees);
	}

	@Override
	public byte[] generateEmployeesExcelForManagerOfficer(String managerOfficer) {
		List<Employee> employees = employeeRepo.findByPmoEmail(managerOfficer);
		if(employees==null) {
			throw new RuntimeErrorException(null, "There is no employee");
		}
		else {
			try {
				return ExcelGenerator.generateReadOnly(employees);
			} catch (IOException e) {
				throw new RuntimeErrorException(null, "Failed to generate Excel : "+e.getMessage());
			}
		}
	}

	@Override
	public byte[] generateEmployeesExcelHr() {
		List<Employee> employees = employeeRepo.findAll();
		if(employees==null) {
			throw new RuntimeErrorException(null, "There is no employee");
		}
		else {
			try {
				return ExcelGenerator.generateReadOnly(employees);
			} catch (IOException e) {
				throw new RuntimeErrorException(null, "Failed to generate Excel : "+e.getMessage());
			}
		}
	}

    @Override
    public List<String> getAllEmployeeIds() {
        return employeeRepo.findAll().stream().map(e -> e.getEmployeeId()).collect(Collectors.toList());
    }
}



