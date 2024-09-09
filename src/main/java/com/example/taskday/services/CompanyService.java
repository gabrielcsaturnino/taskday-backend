package com.example.taskday.services;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyRegisterDTO;
import com.example.taskday.domain.company.CompanyResponseDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.mappers.CompanyMapper;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;

import com.example.taskday.repositories.EmployeeRepository;
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


    public void createCompany(CompanyRegisterDTO companyRegisterDTO, String encryptedPassword) {
        Company company = CompanyMapper.registerDTOToCompany(companyRegisterDTO);
        company.setPassword(encryptedPassword);
        companyRepository.save(company);
    }

    public List<CompanyResponseDTO> seeAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies
                .stream()
                .map(company -> CompanyMapper.toResponseDTO(company)).collect(Collectors.toList());

    }

}

