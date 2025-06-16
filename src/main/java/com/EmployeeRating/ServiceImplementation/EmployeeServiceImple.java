package com.EmployeeRating.ServiceImplementation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Override
	public EmployeeDto save(Employee employee) {
		EmployeeDto dto = mapper.map(employeeRepo.save(employee),EmployeeDto.class);
		return dto;
	}
}
