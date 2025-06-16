package com.EmployeeRating;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EmployeeRatingApplication extends SpringBootServletInitializer{

	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	        return builder.sources(EmployeeRatingApplication.class);
	    }
	public static void main(String[] args) {
		SpringApplication.run(EmployeeRatingApplication.class, args);
	}
	@Bean
	ModelMapper getMapper() {
		return new ModelMapper();
	}
}
