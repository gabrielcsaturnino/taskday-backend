package com.example.taskday.controllers;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.EmployeeRegisteredDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.services.CompanyService;
import com.example.taskday.services.JobVacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/job")
public class JobVacancyController {
    @Autowired
    private JobVacancyService jobVacancyService;

    @Autowired
    CompanyService companyService;

    @PostMapping("/addJobVacancy")
    public ResponseEntity add(@RequestBody @Validated JobVacancyRequestDTO jobVacancyRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.createJobVacancy(jobVacancyRequestDTO, company);
        return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.CREATED));
    }
    @GetMapping("/seeAllJobVacancyForCompany")
    public ResponseEntity<List<JobVacancyResponseDTO>> seeJobVacancy(@RequestParam UUID companyId) {
        List<JobVacancyResponseDTO> jobVacancyResponseDTOList = jobVacancyService.getJobVacanciesByCompany(companyId);
        return ResponseEntity.ok(jobVacancyResponseDTOList);
    }

    @GetMapping("/seeEmployeeForJobVacancy")
    public ResponseEntity<List<EmployeeRegisteredDTO>> seeEmployeeForJobVacancy(@RequestParam("jobVacancyId") UUID jobVacancyId) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Company company = (Company) authentication.getPrincipal();
      List<EmployeeRegisteredDTO> employeeRegisteredDTOSList = jobVacancyService.getAllEmployeeRegisteredByJobVacancy(jobVacancyId);
      return ResponseEntity.ok(employeeRegisteredDTOSList);
    }

    @DeleteMapping("/deleteJobVacancy")
    @Transactional
    public void deleteJobVacancy(@RequestParam("jobVacancyId") UUID jobVacancyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.deleteJobVacancy(jobVacancyId, company);
    }


}
