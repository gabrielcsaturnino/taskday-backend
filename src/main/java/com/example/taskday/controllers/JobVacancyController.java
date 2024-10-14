package com.example.taskday.controllers;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancyResponseDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancyChangeRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyCreateRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyFiltersRequestDTO;
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
    public ResponseEntity<Void> addJobVacancy(@RequestBody @Valid JobVacancyCreateRequestDTO jobVacancyCreateRequestDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.createJobVacancy(jobVacancyCreateRequestDTO, company);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<JobVacancyResponseDTO>> getAllJobVacancies(@RequestParam String companyName) throws OperationException {
        List<JobVacancyResponseDTO> jobVacancies = jobVacancyService.getJobVacanciesByCompany(companyName);
        return ResponseEntity.ok(jobVacancies);
    }

    //@GetMapping("/filter")
    //public ResponseEntity<List<JobVacancyResponseDTO>> getFiltredJobVacancies(@RequestBody JobVacancyFiltersRequestDTO jobVacancyFiltersRequestDTO){

   // }

    @GetMapping("/{jobVacancyId}/employees")
    public ResponseEntity<List<EmployeeJobVacancyResponseDTO>> getRegisteredEmployees(@PathVariable UUID jobVacancyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        List<EmployeeJobVacancyResponseDTO> registeredEmployees = employeeJobVacancyService.getAllEmployeeRegisteredByJobVacancy(jobVacancyId);
        return ResponseEntity.ok(registeredEmployees);
    }

    @GetMapping("/{jobVacancyId}/employees/points")
    public ResponseEntity<List<EmployeeJobVacancyResponseDTO>> getEmployeesByPoints(
            @PathVariable UUID jobVacancyId,
            @RequestParam double minPoints) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        List<EmployeeJobVacancyResponseDTO> employeesByPoints = employeeJobVacancyService.getEmployeesByJobVacancyAndPoints(jobVacancyId, minPoints);

        return ResponseEntity.ok(employeesByPoints);
    }

    @DeleteMapping("/{jobVacancyId}")
    public ResponseEntity<Void> deleteJobVacancy(@PathVariable UUID jobVacancyId) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.deleteJobVacancy(jobVacancyId, company);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{jobVacancyId}")
    public ResponseEntity<Void> updateJobVacancy(@PathVariable UUID jobVacancyId, @RequestBody @Valid JobVacancyChangeRequestDTO jobVacancyChange) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.changeJobVacancy(jobVacancyId, jobVacancyChange, company);
        return ResponseEntity.ok().build();
    }


}

