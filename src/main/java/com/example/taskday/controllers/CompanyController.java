package com.example.taskday.controllers;


import com.example.taskday.domain.company.Company;
import com.example.taskday.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @DeleteMapping("/deleteJobVacancy")
    @Transactional
    public void deleteJobVacancy(@RequestParam("jobVacancyId") UUID jobVacancyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        companyService.deleteJobVacancy(jobVacancyId, company);
    }
}
