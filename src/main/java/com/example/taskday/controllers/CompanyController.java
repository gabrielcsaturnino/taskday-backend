package com.example.taskday.controllers;


import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyChangeAccountDTO;
import com.example.taskday.domain.company.CompanyRequestDTO;
import com.example.taskday.domain.company.CompanyResponseDTO;
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
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;


    @GetMapping("/getAllCompanies")
    public ResponseEntity getAllCompanies() {
        List<CompanyResponseDTO> companiesId = companyService.seeAllCompanies();
        return ResponseEntity.ok(companiesId);
    }


    @PutMapping("/changeAccount")
    public void changeAccount(CompanyChangeAccountDTO companyChangeAccountDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        companyService.changeAccount(companyChangeAccountDTO, company);
    }

    @PutMapping("/changePassword")
    public void changePassword(@RequestBody @Valid String password) throws OperationException {
        if(password.length() <= 10){
            throw new OperationException("Chave deve conter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        companyService.changePassword(encryptedPassword, company);
    }



}
