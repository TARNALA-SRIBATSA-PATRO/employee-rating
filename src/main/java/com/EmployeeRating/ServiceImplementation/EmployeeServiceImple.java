package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;
import java.util.List;

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
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Service.EmployeeService;
@Service
public class EmployeeServiceImple implements EmployeeService{
	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	ModelMapper mapper;
	@Autowired
	EntityManager entityManager;
	@Override
	public EmployeeDto save(Employee employee) {
		EmployeeDto dto = mapper.map(employeeRepo.save(employee),EmployeeDto.class);
		return dto;
	}
	@Override
	public ResponseEntity<?> fetchAll() {
		List<Employee> employees = employeeRepo.findAll();
		return new ResponseEntity<List<Employee>> (employees,HttpStatus.OK);
	}
	@Override
	public ResponseEntity<?> getEmployee(LocalDate date) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> root =cq.from(Employee.class);
		cq.select(root).where(cb.equal(root.get("startDate"),date));
		TypedQuery<Employee> query = entityManager.createQuery(cq);
		List<Employee> result = query.getResultList();
		return new ResponseEntity<List<Employee>>(result,HttpStatus.OK);
		
	}
	@Override
	public ResponseEntity<?> getByCriteria(String managerEmail) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> root = cq.from(Employee.class);
		cq.select(root).where(cb.equal(root.get("managerEmail"),managerEmail));
		
		TypedQuery<Employee> query = entityManager.createQuery(cq);
		
		List<Employee> employees = query.getResultList();
		return new ResponseEntity<List<Employee>>(employees,HttpStatus.OK);
	}
}
