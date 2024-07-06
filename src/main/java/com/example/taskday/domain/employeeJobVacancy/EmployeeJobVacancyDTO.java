package com.example.taskday.domain.employeeJobVacancy;

import java.util.UUID;

public record EmployeeJobVacancyDTO(UUID jobVacancyId, int totalHoursJob, String title, String description) {
}
