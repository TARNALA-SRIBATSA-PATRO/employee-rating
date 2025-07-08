package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Dto.EmployeeDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeRatingTracker;
import com.EmployeeRating.Repository.EmployeeRatingTrackerRepo;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Service.EmployeeService;

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
}



