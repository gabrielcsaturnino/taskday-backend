package com.example.taskday.domain.jobVacancy;

import java.util.UUID;

public record JobVacancySubscribeDTO(UUID jobVacancyId, UUID employeeId, double points) {
}
