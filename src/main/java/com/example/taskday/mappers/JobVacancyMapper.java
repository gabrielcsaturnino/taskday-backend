package com.example.taskday.mappers;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyCreateRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;


public class JobVacancyMapper {

    public static JobVacancy requestDTOToJobVacancy(JobVacancyCreateRequestDTO requestDTO, Company company) {
        JobVacancy jobVacancy = new JobVacancy();
        jobVacancy.setTitle(requestDTO.title());
        jobVacancy.setDescription(requestDTO.description());
        jobVacancy.setCompany(company);
        jobVacancy.setDayValue(requestDTO.dayValue());
        jobVacancy.setTotalHoursJob(requestDTO.totalHoursJob());
        jobVacancy.setDesiredExperience(requestDTO.desiredExperience());
        jobVacancy.setJobDate(requestDTO.jobDate());
        jobVacancy.setCity(requestDTO.city());
        jobVacancy.setState(requestDTO.state());
        jobVacancy.setStatus(requestDTO.status());
        return jobVacancy;
    }

    public static JobVacancyResponseDTO listJobVacancy(JobVacancy jobVacancy){
        JobVacancyResponseDTO jobVacancyResponseDTO = JobVacancyResponseDTO.listToCompany(
                jobVacancy.getId(),
                jobVacancy.getCompany().getName(),
                jobVacancy.getTotalHoursJob(),
                jobVacancy.getTitle(),
                jobVacancy.getDescription(),
                jobVacancy.getDesiredExperience(),
                jobVacancy.getDayValue(),
                jobVacancy.getJobDate(),
                jobVacancy.getCity(),
                jobVacancy.getState(),
                jobVacancy.getStatus()
        );
        return jobVacancyResponseDTO;
    }
}


