package com.example.taskday.domain.jobVacancy;


import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;

import java.time.LocalDate;
import java.util.List;

public record JobVacancyRequestDTO(int totalHoursJob, String title, String description, List<String> desiredExperience, Double dayValue, String status, LocalDate jobDate, List<EmployeeJobVacancy> registeredEmployees, Company company) {
}

