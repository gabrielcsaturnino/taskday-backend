package com.example.taskday.mappers;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyCreateRequestDTO;
import com.example.taskday.domain.company.CompanyResponseDTO;
import com.example.taskday.domain.exceptions.OperationException;


public class CompanyMapper {
    public static CompanyResponseDTO completeCompany(Company company) {
        CompanyResponseDTO companyResponseDTO =  CompanyResponseDTO.completeCompany(
                company.getId(),
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
                company.getOwnerName()
        );
        return companyResponseDTO;
    }

    public static CompanyResponseDTO listCompany(Company company) {
        CompanyResponseDTO companyResponseDTO =  CompanyResponseDTO.partialCompany(
                company.getId(),
                company.getName(),
                company.getAddress(),
                company.getCity(),
                company.getState()

        );
        return companyResponseDTO;
    }

    public static Company registerDTOToCompany(CompanyCreateRequestDTO companyCreateRequestDTO) throws OperationException {
        Company company = new Company();
        company.setName(companyCreateRequestDTO.name());
        company.setCnpj(companyCreateRequestDTO.cnpj());
        company.setPassword(companyCreateRequestDTO.password());
        company.setAddress(companyCreateRequestDTO.address());
        company.setCity(companyCreateRequestDTO.city());
        company.setState(companyCreateRequestDTO.state());
        company.setAddressComplement(companyCreateRequestDTO.addressComplement());
        company.setPostalCode(companyCreateRequestDTO.postalCode());
        company.setOwnerCpf(companyCreateRequestDTO.ownerCpf());
        company.setOwnerName(companyCreateRequestDTO.ownerName());
        company.setEmail(companyCreateRequestDTO.email());
        company.setPhoneNumber(companyCreateRequestDTO.phoneNumber());
        company.setAddressStreet(companyCreateRequestDTO.addressStreet());
        company.setAddressNumber(companyCreateRequestDTO.addressNumber());
        return company;
    }

}
