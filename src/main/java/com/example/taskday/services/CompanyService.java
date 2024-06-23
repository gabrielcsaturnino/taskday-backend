package com.example.taskday.services;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyRequestDTO;
import com.example.taskday.domain.company.CompanyResponseDTO;
import com.example.taskday.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    public void createCompany(Company company) {
        companyRepository.save(company);
    }

    public Company convertToCompany(CompanyRequestDTO companyRequestDTO) {
        return new Company(
                companyRequestDTO.name(),
                companyRequestDTO.cnpj(),
                companyRequestDTO.addressStreet(),
                companyRequestDTO.addressComplement(),
                companyRequestDTO.addressNumber(),
                companyRequestDTO.address(),
                companyRequestDTO.city(),
                companyRequestDTO.state(),
                companyRequestDTO.postalCode(),
                companyRequestDTO.password(),
                companyRequestDTO.email(),
                companyRequestDTO.phoneNumber(),
                companyRequestDTO.ownerName(),
                companyRequestDTO.jobList());

    }

    public CompanyResponseDTO convertToCompanyResponseDTO(Company company) {
        return new CompanyResponseDTO(
                company.getName(),
                company.getCnpj(),
                company.getAddressStreet(),
                company.getAddressComplement(),
                company.getAddressNumber(),
                company.getAddress(),
                company.getCity(),
                company.getState(),
                company.getPostalCode(),
                company.getPassword(),
                company.getEmail(),
                company.getPhoneNumber(),
                company.getOwnerName(),
                company.getJobList()
        );
    }
}
