# Employee Rating System - Deployment Summary

## WAR File
Location: target/employee.war
Status: Ready for deployment

## Setup Steps

1. Deploy WAR File
   - Copy employee.war to application server

2. Configure Database
   - Set DB_URL=jdbc:mysql://your-server:3306/employeerating
   - Set DB_USERNAME=your_username
   - Set DB_PASSWORD=your_sql_password

3. Configure Email
   - Set MAIL_USERNAME=your_email@gmail.com
   - Set MAIL_PASSWORD=your_app_password

4. Set Profile
   - Set spring.profiles.active=prod

5. Start Application
   - Deploy and start the application server

## Email Schedule
Date: 25th of every month
Time: 11:00 AM
Automatic: No manual intervention needed

## Requirements
Java 17
MySQL 8.0+
Gmail account with app password 