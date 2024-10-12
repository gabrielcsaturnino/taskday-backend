package com.example.taskday.controllers;


import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyChangeAccountRequestDTO;
import com.example.taskday.domain.company.CompanyResponseDTO;
import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.PasswordChangeRequestDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.services.CompanyService;
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
}

