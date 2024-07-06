package com.example.taskday.services;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyRegisterDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.mappers.CompanyMapper;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;

import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EmployeeJobVacancyRepository employeeJobVacancyRepository;

    @Autowired
    JobVacancyRepository jobVacancyRepository;




    public void createCompany(CompanyRegisterDTO companyRegisterDTO, String encryptedPassword) {
        if(this.companyRepository.findByEmail(companyRegisterDTO.email()) == null){
            Company company = CompanyMapper.registerDTOToCompany(companyRegisterDTO);
            company.setPassword(encryptedPassword);
            companyRepository.save(company);
        }else {
            throw new RuntimeException("Email ja existente no banco de dados");
        }

    }

    public void deleteJobVacancy(UUID jobVacancyId, Company company) {
        List<EmployeeJobVacancy> employeeJobVacancy = employeeJobVacancyRepository.findByJobVacancy_Id(jobVacancyId);
        Optional<JobVacancy> jobVacancy =  jobVacancyRepository.findById(jobVacancyId);
        company.getJobList().remove(jobVacancy.get());
        jobVacancyRepository.delete(jobVacancy.get());
        companyRepository.save(company);
        employeeJobVacancyRepository.deleteAll(employeeJobVacancy);
    }



}

