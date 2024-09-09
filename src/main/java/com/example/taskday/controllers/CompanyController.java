package com.example.taskday.controllers;


import com.example.taskday.domain.company.CompanyResponseDTO;
import com.example.taskday.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

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

}
