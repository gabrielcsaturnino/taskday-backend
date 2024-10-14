package com.example.taskday.controllers;


import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyChangeAccountRequestDTO;
import com.example.taskday.domain.company.CompanyResponseDTO;
import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.PasswordChangeRequestDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.repositories.JobVacancyRepository;
import com.example.taskday.services.CompanyService;
import com.example.taskday.services.JobVacancyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private JobVacancyRepository jobVacancyRepository;
    @Autowired
    private JobVacancyService jobVacancyService;

    @GetMapping()
    public ResponseEntity<List<CompanyResponseDTO>> getAllCompanies() {
        List<CompanyResponseDTO> companies = companyService.seeAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/account")
    public void updateAccount(@RequestBody @Valid CompanyChangeAccountRequestDTO companyChangeAccountRequestDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        companyService.changeAccount(companyChangeAccountRequestDTO, company);
    }


    @PutMapping("/password")
    public void updatePassword(@RequestBody @Valid PasswordChangeRequestDTO passwordChangeRequestDTO) throws OperationException {
        if (passwordChangeRequestDTO.password().length() <= 10) {
            throw new OperationException("A senha deve ter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(passwordChangeRequestDTO.password());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        companyService.changePassword(encryptedPassword, company, passwordChangeRequestDTO.code());
    }

    @PostMapping("/request-password-change")
    public ResponseEntity<?> requestPasswordChange() throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        companyService.resendConfirmationCode(company.getEmail());
        return ResponseEntity.ok("Código enviado!");
    }


    @GetMapping("/me")
    public ResponseEntity<CompanyResponseDTO> getCompanyProfile() throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        CompanyResponseDTO companyResponseDTO = companyService.findCompanyById(company);
        return ResponseEntity.ok().body(companyResponseDTO);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobVacancyResponseDTO>> myJobs(){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       Company company = (Company) authentication.getPrincipal();
       List<JobVacancyResponseDTO> jobVacancyResponseDTOS = jobVacancyService.myJobs(company);
       return ResponseEntity.ok().body(jobVacancyResponseDTOS);
    }

    @DeleteMapping
    public void deleteAccount() throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();

    }
}

