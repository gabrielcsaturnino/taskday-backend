package com.example.taskday.controllers;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.services.JobVacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobVacancyController {
    @Autowired
    private JobVacancyService jobVacancyService;

    @PostMapping("/addJobVacancy")
    public ResponseEntity add(@RequestBody @Validated JobVacancyRequestDTO jobVacancyRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.createJobVacancy(jobVacancyRequestDTO, company);
        return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.CREATED));
    }
    @GetMapping("/see")
    public ResponseEntity<List<JobVacancyResponseDTO>> seeJobVacancy() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        List<JobVacancyResponseDTO> jobVacancyResponseDTOList = jobVacancyService.getJobVacanciesByCompany(company.getId());
        return ResponseEntity.ok(jobVacancyResponseDTOList);
    }
}
