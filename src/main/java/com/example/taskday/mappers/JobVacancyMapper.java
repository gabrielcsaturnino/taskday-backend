package com.example.taskday.mappers;

import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;

import java.util.stream.Collectors;


public class JobVacancyMapper {
    public static JobVacancyResponseDTO toDTOJobVacancy(JobVacancy jobVacancy) {
        JobVacancyResponseDTO jobVacancyResponseDTO = new JobVacancyResponseDTO(
                jobVacancy.getId(),
                jobVacancy.getTotalHoursJob(),
                jobVacancy.getTitle(),
                jobVacancy.getDescription(),
                jobVacancy.getDesiredExperience(),
                jobVacancy.getDayValue(),
                jobVacancy.getStatus(),
                jobVacancy.getJobDate(),
                jobVacancy.getCompany().getId(),
                jobVacancy.getRegisteredEmployees()
                        .stream()
                        .map(employeeJobVacancy -> EmployeeJobVacancyMapper.listEmployeeToJobVacancyResponseDTO(employeeJobVacancy.getEmployee())).collect(Collectors.toList()));


        return jobVacancyResponseDTO;
    }



    public static JobVacancy requestDTOToJobVacancy(JobVacancyRequestDTO requestDTO) {
        JobVacancy jobVacancy = new JobVacancy();
        jobVacancy.setJobDate(requestDTO.jobDate());
        jobVacancy.setTitle(requestDTO.title());
        jobVacancy.setDescription(requestDTO.description());
        jobVacancy.setStatus(requestDTO.status());
        jobVacancy.setCompany(requestDTO.company());
        jobVacancy.setDayValue(requestDTO.dayValue());
        jobVacancy.setTotalHoursJob(requestDTO.totalHoursJob());
        jobVacancy.setDesiredExperience(requestDTO.desiredExperience());
        jobVacancy.setStatus(requestDTO.status());
        jobVacancy.setJobDate(requestDTO.jobDate());
        return jobVacancy;
    }




}
