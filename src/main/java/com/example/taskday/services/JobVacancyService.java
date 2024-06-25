package com.example.taskday.services;

import com.example.taskday.domain.employee.EmployeeRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobVacancyService {

    @Autowired
    JobVacancyRepository jobVacancyRepository;

    public void createJobVacancy(JobVacancyRequestDTO jobVacancyRequestDTO) {
        JobVacancy jobVacancy = this.convertToJobVacancy(jobVacancyRequestDTO);
        jobVacancyRepository.save(jobVacancy);
    }

    public List<JobVacancyResponseDTO> getAllJobVacancy(){
        return jobVacancyRepository.findAll()
                .stream()
                .map(this::convertToJobVacancyResponseDTO).collect(Collectors.toList());
    }


    public JobVacancy convertToJobVacancy(JobVacancyRequestDTO jobVacancyRequestDTO) {
        return new JobVacancy(
                jobVacancyRequestDTO.totalHoursJob(),
                jobVacancyRequestDTO.title(),
                jobVacancyRequestDTO.description(),
                jobVacancyRequestDTO.desiredExperience(),
                jobVacancyRequestDTO.dayValue(),
                jobVacancyRequestDTO.status(),
                jobVacancyRequestDTO.jobDate()

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
                jobVacancy.getJobDate()

        );
    }

}
