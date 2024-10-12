package com.example.taskday.domain.jobVacancy;

import com.example.taskday.enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record JobVacancyChangeRequestDTO(Optional<Integer> totalHoursJob, Optional<String> title, Optional<String> description, Optional<List<String>> desiredExperience, Optional<Double> dayValue, Optional<LocalDate> jobDate, Optional<String> city, Optional<String> state, Optional<Status> status) {
}
