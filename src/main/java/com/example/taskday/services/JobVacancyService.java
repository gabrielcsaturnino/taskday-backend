package com.example.taskday.services;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.EmployeeRegisteredDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.mappers.EmployeeMapper;
import com.example.taskday.mappers.JobVacancyMapper;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;
import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobVacancyService {

    @Autowired
    JobVacancyRepository jobVacancyRepository;

    @Autowired
    EmployeeJobVacancyRepository employeeJobVacancyRepository;

    @Autowired
    CompanyRepository companyRepository;

    /*
     * Correto!
     * */
    public void createJobVacancy(JobVacancyRequestDTO jobVacancyRequestDTO, Company company) {
        try {
            JobVacancy jobVacancy = JobVacancyMapper.requestDTOToJobVacancy(jobVacancyRequestDTO, company);
            jobVacancy.setCompany(company);
            jobVacancyRepository.save(jobVacancy);
        }catch (NullPointerException e){
            throw new RuntimeException("Company id not found", e);
        }
    }



    /*
     * Correto!
     * */
    public List<JobVacancyResponseDTO> getJobVacanciesByCompany(String name) {
        Optional<Company> companyOptional = companyRepository.findByName(name);
        UUID companyId = companyOptional.get().getId();
        List<JobVacancyResponseDTO> jobVacancyResponseDTOList =  jobVacancyRepository.findByCompany_Id(companyId).
                stream().map(JobVacancyMapper::toDTOJobVacancy).collect(Collectors.toList());
        return jobVacancyResponseDTOList;
    }

    /*
     * Correto!
     * */
    public void deleteJobVacancy(UUID jobVacancyId, Company company) {
        List<EmployeeJobVacancy> employeeJobVacancy = employeeJobVacancyRepository.findByJobVacancy_Id(jobVacancyId);
        Optional<JobVacancy> jobVacancy =  jobVacancyRepository.findById(jobVacancyId);
        company.getJobList().remove(jobVacancy.get());
        jobVacancyRepository.delete(jobVacancy.get());
        companyRepository.save(company);
        employeeJobVacancyRepository.deleteAll(employeeJobVacancy);
    }




}
