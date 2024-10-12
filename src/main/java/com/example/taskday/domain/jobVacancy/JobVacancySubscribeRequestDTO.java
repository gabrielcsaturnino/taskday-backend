package com.example.taskday.domain.jobVacancy;

import java.util.UUID;

public record JobVacancySubscribeRequestDTO(UUID jobVacancyId, UUID employeeId) {
}
