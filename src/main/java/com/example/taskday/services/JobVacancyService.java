package com.example.taskday.services;

import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class JobVacancyService {

    @Autowired
    JobVacancyRepository jobVacancyRepository;

    public JobVacancy convertToJobVacancy(JobVacancyRequestDTO jobVacancyRequestDTO) {
        return new JobVacancy(
                jobVacancyRequestDTO.totalHoursJob(),
                jobVacancyRequestDTO.title(),
                jobVacancyRequestDTO.description(),
                jobVacancyRequestDTO.desiredExperience(),
                jobVacancyRequestDTO.dayValue(),
                jobVacancyRequestDTO.status(),
                jobVacancyRequestDTO.jobDate(),
                jobVacancyRequestDTO.registeredEmployees(),
                jobVacancyRequestDTO.company()
        );
    }

    public JobVacancyResponseDTO convertToJobVacancyResponseDTO(JobVacancy jobVacancy) {
        return new JobVacancyResponseDTO(
                jobVacancy.getTotalHoursJob(),
                jobVacancy.getTitle(),
                jobVacancy.getDescription(),
                jobVacancy.getDesiredExperience(),
                jobVacancy.getDayValue(),
                jobVacancy.getStatus(),
                jobVacancy.getJobDate(),
                jobVacancy.getRegisteredEmployees(),
                jobVacancy.getCompany()
        );
    }

}
