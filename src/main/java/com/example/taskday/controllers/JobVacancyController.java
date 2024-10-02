package com.example.taskday.controllers;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.EmployeeRegisteredDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancyChange;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.services.EmployeeJobVacancyService;
import com.example.taskday.services.JobVacancyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/job-vacancies")
public class JobVacancyController {

    @Autowired
    private JobVacancyService jobVacancyService;

    @Autowired
    private EmployeeJobVacancyService employeeJobVacancyService;

    @PostMapping
    public ResponseEntity<Void> addJobVacancy(@RequestBody @Valid JobVacancyRequestDTO jobVacancyRequestDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.createJobVacancy(jobVacancyRequestDTO, company);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<JobVacancyResponseDTO>> getAllJobVacancies(@RequestParam String companyName) throws OperationException {
        List<JobVacancyResponseDTO> jobVacancies = jobVacancyService.getJobVacanciesByCompany(companyName);
        return ResponseEntity.ok(jobVacancies);
    }

    @GetMapping("/{jobVacancyId}/employees")
    public ResponseEntity<List<EmployeeRegisteredDTO>> getRegisteredEmployees(@PathVariable UUID jobVacancyId) {
        List<EmployeeRegisteredDTO> registeredEmployees = employeeJobVacancyService.getAllEmployeeRegisteredByJobVacancy(jobVacancyId);
        return ResponseEntity.ok(registeredEmployees);
    }

    @DeleteMapping("/{jobVacancyId}")
    public ResponseEntity<Void> deleteJobVacancy(@PathVariable UUID jobVacancyId) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.deleteJobVacancy(jobVacancyId, company);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{jobVacancyId}")
    public ResponseEntity<Void> updateJobVacancy(@PathVariable UUID jobVacancyId, @RequestBody @Valid JobVacancyChange jobVacancyChange) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.changeJobVacancy(jobVacancyId, jobVacancyChange, company);
        return ResponseEntity.ok().build();
    }
}

