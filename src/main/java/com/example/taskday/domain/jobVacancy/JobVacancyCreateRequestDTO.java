package com.example.taskday.domain.jobVacancy;


import com.example.taskday.enums.Status;

import java.time.LocalDate;
import java.util.List;

public record JobVacancyCreateRequestDTO(int totalHoursJob, String title,
                                         String description, List<String> desiredExperience,
                                         Double dayValue, LocalDate jobDate, String city,
                                         String state, Status status) {
}

