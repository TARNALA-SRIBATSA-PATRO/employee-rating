package com.EmployeeRating.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EmployeeRating.Entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee,Long>{

}
