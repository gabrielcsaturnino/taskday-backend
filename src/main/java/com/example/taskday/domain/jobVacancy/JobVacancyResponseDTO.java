package com.example.taskday.domain.jobVacancy;

import com.example.taskday.enums.Status;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record JobVacancyResponseDTO(Optional<UUID> uuid, Optional<String> companyName,Optional<Integer> totalHoursJob, Optional<String> title, Optional<String> description, Optional<List<String>> desiredExperience, Optional<Double> dayValue, Optional<LocalDate> jobDate, Optional<String> city, Optional<String> state, Optional<Status> status) {

    public static JobVacancyResponseDTO listToCompany(UUID uuid,String companyName, Integer totalHoursJob, String title, String description, List<String> desiredExperience, Double dayValue, LocalDate jobDate, String city, String state, Status status) {
        return new JobVacancyResponseDTO(Optional.of(uuid), Optional.of(companyName), Optional.of(totalHoursJob), Optional.of(title), Optional.of(description), Optional.of(desiredExperience), Optional.of(dayValue), Optional.of(jobDate), Optional.of(city), Optional.of(state), Optional.of(status));
    }

    public static JobVacancyResponseDTO listToEmployee(UUID uuid,String companyName, Integer totalHoursJob, String title, String description, List<String> desiredExperience, Double dayValue, LocalDate jobDate, String city, Status status) {
        return new JobVacancyResponseDTO(Optional.of(uuid), Optional.of(companyName), Optional.of(totalHoursJob), Optional.of(title), Optional.of(description), Optional.of(desiredExperience), Optional.of(dayValue), Optional.of(jobDate), Optional.of(city), Optional.empty(), Optional.of(status));
    }
}
