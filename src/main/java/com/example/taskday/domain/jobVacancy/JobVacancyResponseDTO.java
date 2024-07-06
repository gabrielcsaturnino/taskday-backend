package com.example.taskday.domain.jobVacancy;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.employeeJobVacancy.JobVacancyEmployeeDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record JobVacancyResponseDTO(UUID jobVacancyId,int totalHoursJob, String title, String description, List<String> desiredExperience, Double dayValue, String status, LocalDate jobDate, UUID compantId, List<JobVacancyEmployeeDTO> registeredEmployeeJobVacancyId) {
}
