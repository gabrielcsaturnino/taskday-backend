package com.example.taskday.domain.employeeJobVacancy;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.jobVacancy.JobVacancy;

public record EmployeeJobVacancyResponseDTO(Employee employee, JobVacancy jobVacancy) {
}
