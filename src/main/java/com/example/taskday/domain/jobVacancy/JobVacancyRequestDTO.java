package com.example.taskday.domain.jobVacancy;


import com.example.taskday.domain.employee.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record JobVacancyRequestDTO(String title, String description, List<String> desiredExperience, Double dayValue, String status, LocalDate jobDate, int totalHoursJob, List<Employee> registeredEmployee) {
}

