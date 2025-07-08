package com.EmployeeRating.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EmployeeRating.Entity.EmployeeRatingTracker;
@Repository
public interface EmployeeRatingTrackerRepo extends JpaRepository<EmployeeRatingTracker,Long>{

}
