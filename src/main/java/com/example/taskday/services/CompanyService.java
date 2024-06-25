package com.example.taskday.services;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyRegisterDTO;
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

    public void createCompany(CompanyRegisterDTO companyRegisterDTO, String encryptedPassword) {
        Company company = companyRegisterDTOtoCompany(companyRegisterDTO);
        company.setPassword(encryptedPassword);
        companyRepository.save(company);
    }

    public Company companyRegisterDTOtoCompany(CompanyRegisterDTO companyRegisterDTO) {
        return new Company(
                companyRegisterDTO.name(),
                companyRegisterDTO.cnpj(),
                companyRegisterDTO.addressStreet(),
                companyRegisterDTO.addressComplement(),
                companyRegisterDTO.addressNumber(),
                companyRegisterDTO.address(),
                companyRegisterDTO.city(),
                companyRegisterDTO.state(),
                companyRegisterDTO.postalCode(),
                companyRegisterDTO.password(),
                companyRegisterDTO.email(),
                companyRegisterDTO.phoneNumber(),
                companyRegisterDTO.ownerName());

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
