package com.EmployeeRating.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EmployeeRating.Entity.Employee;
@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long>{
	Optional<Employee> findByEmployeeId(String empId);
}
