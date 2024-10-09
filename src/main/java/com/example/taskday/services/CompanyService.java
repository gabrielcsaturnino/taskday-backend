package com.example.taskday.services;

import com.example.taskday.domain.company.*;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.exceptions.OperationException;

import com.example.taskday.enums.RoleType;
import com.example.taskday.mappers.CompanyMapper;
import com.example.taskday.repositories.CompanyRepository;

import com.example.taskday.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmailService emailService;


    public void createCompany(CompanyCreateRequestDTO companyCreateRequestDTO, String encryptedPassword) throws OperationException {
        if(companyRepository.existsByEmail(companyCreateRequestDTO.email()) || companyRepository.existsByName(companyCreateRequestDTO.name()) || companyRepository.existsByCnpj(companyCreateRequestDTO.cnpj())){
           throw new OperationException("Empresa ja cadastrada");
        }


        Company company = CompanyMapper.registerDTOToCompany(companyCreateRequestDTO);
        company.setPassword(encryptedPassword);
        company.setCreatedBy(LocalDate.now());
        company.setUpdatedBy(LocalDate.now());

        company.setConfirmationCode(generateConfirmationCode());
        emailService.sendEmail(company.getEmail(), company.getConfirmationCode());
        company.setRoleType(RoleType.INACTIVE);
        company.setEnabled(true);
        companyRepository.save(company);
    }

    public void confirmationAccount(Company company, String code) throws OperationException {
        if(company.getConfirmationCode().equals(code)){
            company.setRoleType(RoleType.EMPLOYEE);
            company.setConfirmationCode(null);
            company.setEnabled(true);
            companyRepository.save(company);
        }else {
            throw new OperationException("Código de confirmação inválido!");
        }
    }

    public void resendConfirmationCode(String email) throws OperationException {
        Company company = (Company) companyRepository.findByEmail(email);
        if(company.getRoleType() == RoleType.INACTIVE){
            company.setConfirmationCode(generateConfirmationCode());
            companyRepository.save(company);
            emailService.sendEmail(email, company.getConfirmationCode());
        }else {
            throw new OperationException("Usuário ja autenticado!");
        }

    }

    private String generateConfirmationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Gera um código de 6 dígitos
        return String.valueOf(code);
    }


    public CompanyResponseDTO findCompanyById(Company company) throws OperationException {
        Optional<Company> company1 = companyRepository.findById(company.getId());
        if (!company1.isPresent()){
            throw new OperationException("Empresa não foi encontrada!");
        }
        CompanyResponseDTO companyResponseDTO = CompanyMapper.completeCompany(company1.get());
        return companyResponseDTO;
    }

    public List<CompanyResponseDTO> seeAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies
                .stream()
                .map(company -> CompanyMapper.listCompany(company)).collect(Collectors.toList());

    }

    public void changeAccount(CompanyChangeAccountRequestDTO companyChangeAccountRequestDTO, Company company) throws OperationException {

        if(companyChangeAccountRequestDTO.email().isPresent()) {
            if (employeeRepository.existsByEmail(companyChangeAccountRequestDTO.email().get())
                    || companyRepository.existsByEmail(companyChangeAccountRequestDTO.email().get())
                    || companyRepository.existsByName(companyChangeAccountRequestDTO.name().get())) {
                throw new OperationException("Empresa ja cadastrada");
            }
        }

        companyChangeAccountRequestDTO.name().ifPresent(company :: setName);
        companyChangeAccountRequestDTO.address().ifPresent(company :: setAddress);
        companyChangeAccountRequestDTO.addressComplement().ifPresent(company :: setAddressComplement);
        companyChangeAccountRequestDTO.addressStreet().ifPresent(company :: setAddressStreet);
        companyChangeAccountRequestDTO.addressNumber().ifPresent(company :: setAddressNumber);
        companyChangeAccountRequestDTO.city().ifPresent(company :: setCity);
        companyChangeAccountRequestDTO.state().ifPresent(company :: setState);
        companyChangeAccountRequestDTO.phoneNumber().ifPresent(company :: setPhoneNumber);
        companyChangeAccountRequestDTO.postalCode().ifPresent(company :: setPostalCode);
        companyChangeAccountRequestDTO.email().ifPresent(company :: setEmail);
        companyChangeAccountRequestDTO.name().ifPresent(company :: setName);
        companyChangeAccountRequestDTO.name().ifPresent(company :: setName);
        company.setUpdatedBy(LocalDate.now());
        companyRepository.save(company);
    }

    public void changePassword(String password, Company company){
        company.setPassword(password);
        company.setUpdatedBy(LocalDate.now());
        companyRepository.save(company);
    }
}

