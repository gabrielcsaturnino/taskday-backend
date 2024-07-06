package com.example.taskday.services;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.EmployeeRegisteredDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.mappers.EmployeeMapper;
import com.example.taskday.mappers.JobVacancyMapper;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;
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

    @Autowired
    EmployeeJobVacancyRepository employeeJobVacancyRepository;

    /*
     * Correto!
     * */
    public void createJobVacancy(JobVacancyRequestDTO jobVacancyRequestDTO, Company company) {
        try {
            JobVacancy jobVacancy = JobVacancyMapper.requestDTOToJobVacancy(jobVacancyRequestDTO);
            jobVacancy.setCompany(company);
            jobVacancyRepository.save(jobVacancy);
        }catch (NullPointerException e){
            throw new RuntimeException("Company id not found", e);
        }
    }





    /*
    * Correto!
    * */
    public List<JobVacancyResponseDTO> getJobVacanciesByCompany(UUID companyId) {
        List<JobVacancyResponseDTO> jobVacancyResponseDTOList =  jobVacancyRepository.findByCompany_Id(companyId).
                stream().map(JobVacancyMapper::toDTOJobVacancy).collect(Collectors.toList());
        return jobVacancyResponseDTOList;
    }

    /*
     * Correto!
     * */
    public List<EmployeeRegisteredDTO> getAllEmployeeRegisteredByJobVacancy(UUID jobVacancyId){

        return employeeJobVacancyRepository.findByJobVacancy_Id(jobVacancyId)
                .stream()
                .map(EmployeeJobVacancy::getEmployee).map(EmployeeMapper :: employeeToEmployeeRegisteredDTO).collect(Collectors.toList());
    }






}
