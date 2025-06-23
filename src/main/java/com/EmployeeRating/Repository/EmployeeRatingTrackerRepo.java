package com.EmployeeRating.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EmployeeRating.Entity.EmployeeRatingTracker;

public interface EmployeeRatingTrackerRepo extends JpaRepository<EmployeeRatingTracker,Long>{

}
