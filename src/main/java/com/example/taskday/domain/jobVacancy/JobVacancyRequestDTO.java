package com.example.taskday.domain.jobVacancy;


import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record JobVacancyRequestDTO(int totalHoursJob, String title, String description, List<String> desiredExperience, Double dayValue, String status, LocalDate jobDate, Company company) {
}

