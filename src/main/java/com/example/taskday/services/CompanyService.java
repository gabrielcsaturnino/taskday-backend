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

        try {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.assertValid(companyRegisterDTO.ownerCpf());
        }catch (Exception e){
           throw new OperationException("CPF invalido");
        }


        try {
            CNPJValidator cnpjValidator = new CNPJValidator();
            cnpjValidator.assertValid(companyRegisterDTO.cnpj());
        }catch (Exception e){
           throw  new OperationException("CNPJ invalido");
        }


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

    public void changeAccount(CompanyChangeAccountDTO companyChangeAccountDTO, Company company) throws OperationException {

        if(companyRepository.existsByEmail(companyChangeAccountDTO.email()) || companyRepository.existsByName(companyChangeAccountDTO.name())){
            throw new OperationException("Empresa ja cadastrada");
        }

        company.setName(companyChangeAccountDTO.name());
        company.setAddress(companyChangeAccountDTO.address());
        company.setAddressComplement(companyChangeAccountDTO.addressComplement());
        company.setAddressNumber(companyChangeAccountDTO.addressNumber());
        company.setAddressStreet(companyChangeAccountDTO.addressStreet());
        company.setCity(companyChangeAccountDTO.city());
        company.setState(companyChangeAccountDTO.state());
        company.setPhoneNumber(companyChangeAccountDTO.phoneNumber());
        company.setPostalCode(companyChangeAccountDTO.postalCode());
        company.setEmail(companyChangeAccountDTO.email());
        companyRepository.save(company);
    }

    public void changePassword(String password, Company company){
        company.setPassword(password);
        companyRepository.save(company);
    }
}

