package com.example.taskday.domain.jobVacancy;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.employeeJobVacancy.JobVacancyEmployeeDTO;
import com.example.taskday.enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record JobVacancyResponseDTO(UUID uuid, int totalHoursJob, String title, String description, List<String> desiredExperience, Double dayValue, LocalDate jobDate, String city, String state, Status status) {
}