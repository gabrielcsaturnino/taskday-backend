package com.example.taskday.controllers;


import com.example.taskday.domain.company.Company;
import com.example.taskday.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;




@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @GetMapping("/getAllCompanies")
    public ResponseEntity getAllCompanies() {
        List<UUID> companiesId = companyService.seeAllCompanies();
        return ResponseEntity.ok(companiesId);
    }

}
