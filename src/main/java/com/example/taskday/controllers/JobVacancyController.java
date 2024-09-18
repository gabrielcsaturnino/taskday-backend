package com.example.taskday.controllers;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.EmployeeRegisteredDTO;
import com.example.taskday.domain.exceptions.OperationException;
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
@RequestMapping("/job")
public class JobVacancyController {
    @Autowired
    private JobVacancyService jobVacancyService;

    @Autowired
    private EmployeeJobVacancyService employeeJobVacancyService;

    @Autowired
    private AuthenticationController authenticationController;

    @PostMapping("/addJobVacancy")
    public ResponseEntity add(@RequestBody @Valid JobVacancyRequestDTO jobVacancyRequestDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.createJobVacancy(jobVacancyRequestDTO, company);
        return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.CREATED));
    }


    @GetMapping("/seeAllJobVacancyForCompany")
    public ResponseEntity<List<JobVacancyResponseDTO>> seeJobVacancy(@RequestParam String name) throws OperationException {
        List<JobVacancyResponseDTO> jobVacancyResponseDTOList = jobVacancyService.getJobVacanciesByCompany(name);
        return ResponseEntity.ok(jobVacancyResponseDTOList);
    }

    @GetMapping("/seeEmployeeForJobVacancy")
    public ResponseEntity<List<EmployeeRegisteredDTO>> seeEmployeeForJobVacancy(@RequestParam("jobVacancyId") UUID jobVacancyId) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Company company = (Company) authentication.getPrincipal();
      List<EmployeeRegisteredDTO> employeeRegisteredDTOSList = employeeJobVacancyService.getAllEmployeeRegisteredByJobVacancy(jobVacancyId);
      return ResponseEntity.ok(employeeRegisteredDTOSList);
    }

    @DeleteMapping("/deleteJobVacancy")
    public void deleteJobVacancy(@RequestParam("jobVacancyId") UUID jobVacancyId) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.deleteJobVacancy(jobVacancyId, company);
    }

    @PutMapping("/changeJobVacancy")
    public ResponseEntity changeJobVacancy(@RequestParam("jobVacancyId") UUID jobVacancyId, @RequestBody @Valid JobVacancyRequestDTO jobVacancyRequestDTO ) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = (Company) authentication.getPrincipal();
        jobVacancyService.changeJobVacancy(jobVacancyId, jobVacancyRequestDTO, company);
        return ResponseEntity.ok().build();
    }


}
