package com.EmployeeRating.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EmployeeRating.Entity.Rating;

public interface RatingRepo extends JpaRepository<Rating, Long>{

}
