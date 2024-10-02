package com.example.taskday.services;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import com.example.taskday.domain.company.*;

import com.example.taskday.domain.exceptions.OperationException;

import com.example.taskday.mappers.CompanyMapper;
import com.example.taskday.repositories.CompanyRepository;

import com.example.taskday.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    public void createCompany(CompanyRegisterDTO companyRegisterDTO, String encryptedPassword) throws OperationException {
        if(companyRepository.existsByEmail(companyRegisterDTO.email()) || companyRepository.existsByName(companyRegisterDTO.name()) || companyRepository.existsByCnpj(companyRegisterDTO.cnpj())){
           throw new OperationException("Empresa ja cadastrada");
        }


        Company company = CompanyMapper.registerDTOToCompany(companyRegisterDTO);
        company.setPassword(encryptedPassword);
        company.setCreatedBy(LocalDate.now());
        company.setUpdatedBy(LocalDate.now());
        companyRepository.save(company);
    }

    public List<CompanyResponseDTO> seeAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies
                .stream()
                .map(company -> CompanyMapper.toResponseDTO(company)).collect(Collectors.toList());

    }

    public void changeAccount(CompanyChangeAccountDTO companyChangeAccountDTO, Company company) throws OperationException {

        if(companyChangeAccountDTO.email().isPresent()) {
            if (employeeRepository.existsByEmail(companyChangeAccountDTO.email().get())
                    || companyRepository.existsByEmail(companyChangeAccountDTO.email().get())
                    || companyRepository.existsByName(companyChangeAccountDTO.name().get())) {
                throw new OperationException("Empresa ja cadastrada");
            }
        }

        companyChangeAccountDTO.name().ifPresent(company :: setName);
        companyChangeAccountDTO.address().ifPresent(company :: setAddress);
        companyChangeAccountDTO.addressComplement().ifPresent(company :: setAddressComplement);
        companyChangeAccountDTO.addressStreet().ifPresent(company :: setAddressStreet);
        companyChangeAccountDTO.addressNumber().ifPresent(company :: setAddressNumber);
        companyChangeAccountDTO.city().ifPresent(company :: setCity);
        companyChangeAccountDTO.state().ifPresent(company :: setState);
        companyChangeAccountDTO.phoneNumber().ifPresent(company :: setPhoneNumber);
        companyChangeAccountDTO.postalCode().ifPresent(company :: setPostalCode);
        companyChangeAccountDTO.email().ifPresent(company :: setEmail);
        companyChangeAccountDTO.name().ifPresent(company :: setName);
        companyChangeAccountDTO.name().ifPresent(company :: setName);
        company.setUpdatedBy(LocalDate.now());
        companyRepository.save(company);
    }

    public void changePassword(String password, Company company){
        company.setPassword(password);
        company.setUpdatedBy(LocalDate.now());
        companyRepository.save(company);
    }
}

