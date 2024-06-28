package com.example.taskday.services;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobVacancyService {

    @Autowired
    JobVacancyRepository jobVacancyRepository;

    public void createJobVacancy(JobVacancyRequestDTO jobVacancyRequestDTO, Company company) {
        try {
            JobVacancy jobVacancy = convertToJobVacancy(jobVacancyRequestDTO);
            jobVacancy.setCompany(company);
            jobVacancyRepository.save(jobVacancy);
        }catch (NullPointerException e){
            throw new RuntimeException("Company id not found", e);
        }

    }

    public List<JobVacancyResponseDTO> getAllJobVacancy(){
        return jobVacancyRepository.findAll()
                .stream()
                .map(this::convertToJobVacancyResponseDTO).collect(Collectors.toList());
    }

    public List<JobVacancyResponseDTO> getJobVacanciesByCompany(UUID companyId) {
        List<JobVacancyResponseDTO> jobVacancyResponseDTOList =  jobVacancyRepository.findByCompany_Id(companyId).
                stream().map(this::convertToJobVacancyResponseDTO).collect(Collectors.toList());
        return jobVacancyResponseDTOList;
    }


    public JobVacancy convertToJobVacancy(JobVacancyRequestDTO jobVacancyRequestDTO) {
        return new JobVacancy(
                jobVacancyRequestDTO.totalHoursJob(),
                jobVacancyRequestDTO.title(),
                jobVacancyRequestDTO.description(),
                jobVacancyRequestDTO.desiredExperience(),
                jobVacancyRequestDTO.dayValue(),
                jobVacancyRequestDTO.status(),
                jobVacancyRequestDTO.jobDate(),
                jobVacancyRequestDTO.company()
        );
    }



    public JobVacancyResponseDTO convertToJobVacancyResponseDTO(JobVacancy jobVacancy) {
        return new JobVacancyResponseDTO(
                jobVacancy.getId(),
                jobVacancy.getTotalHoursJob(),
                jobVacancy.getTitle(),
                jobVacancy.getDescription(),
                jobVacancy.getDesiredExperience(),
                jobVacancy.getDayValue(),
                jobVacancy.getStatus(),
                jobVacancy.getJobDate(),
                jobVacancy.getCompany().getId(),
                jobVacancy.getRegisteredEmployees(),
                jobVacancy.getRegisteredEmployeeIds()

        );
    }

}
