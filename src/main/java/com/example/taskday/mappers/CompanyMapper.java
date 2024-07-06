package com.example.taskday.mappers;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyJobVacancyListDTO;
import com.example.taskday.domain.company.CompanyRegisterDTO;
import com.example.taskday.domain.company.CompanyResponseDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;

import java.util.stream.Collectors;


public class CompanyMapper {
    public static CompanyResponseDTO toResponseDTO(Company company) {
        CompanyResponseDTO companyResponseDTO = new CompanyResponseDTO(
                company.getId(),
                company.getCnpj(),
                company.getName(),
                company.getAddressStreet(),
                company.getAddressComplement(),
                company.getAddressNumber(),
                company.getAddress(),
                company.getCity(),
                company.getState(),
                company.getPostalCode(),
                company.getEmail(),
                company.getPhoneNumber(),
                company.getOwnerName(),
                company.getJobList().stream().map(jobVacancy -> listJobVacancyToCompany(jobVacancy)).collect(Collectors.toList())
        );
        return companyResponseDTO;
    }

    public static Company registerDTOToCompany(CompanyRegisterDTO companyRegisterDTO) {
        Company company = new Company();
        company.setName(companyRegisterDTO.name());
        company.setCnpj(companyRegisterDTO.cnpj());
        company.setPassword(companyRegisterDTO.password());
        company.setAddress(companyRegisterDTO.address());
        company.setCity(companyRegisterDTO.city());
        company.setState(companyRegisterDTO.state());
        company.setAddressComplement(companyRegisterDTO.addressComplement());
        company.setOwnerName(companyRegisterDTO.ownerName());
        company.setEmail(companyRegisterDTO.email());
        company.setPhoneNumber(companyRegisterDTO.phoneNumber());
        company.setAddressStreet(companyRegisterDTO.addressStreet());
        company.setAddressNumber(companyRegisterDTO.addressNumber());
        return company;
    }

    public static CompanyJobVacancyListDTO listJobVacancyToCompany(JobVacancy jobVacancy) {
        CompanyJobVacancyListDTO companyJobVacancyListDTO = new CompanyJobVacancyListDTO(
                jobVacancy.getId(),
                jobVacancy.getTitle(),
                jobVacancy.getDescription()
        );
        return companyJobVacancyListDTO;
    }

}
