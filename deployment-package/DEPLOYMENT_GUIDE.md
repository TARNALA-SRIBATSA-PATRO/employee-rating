# Employee Rating System - Deployment Guide

## WAR File Location
The WAR file has been built and is located at: target/employee.war

## Configuration Changes Needed

Copy application-prod.properties to the same directory as the WAR file and update these values:

Database Configuration
spring.datasource.url=jdbc:mysql://your-db-server:3306/employeerating
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

Email Configuration
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password

Set the Spring profile: spring.profiles.active=prod 