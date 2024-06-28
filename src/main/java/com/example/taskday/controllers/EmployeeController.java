package com.example.taskday.controllers;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRequestDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.domain.jobVacancy.JobVacancySubscribeDTO;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.services.EmployeeJobVacancyService;
import com.example.taskday.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeJobVacancyService employeeJobVacancyService;




    @GetMapping("/allSubscribeJob")
    public ResponseEntity<List<JobVacancyResponseDTO>> getAllSubscribeJob() {
        Authentication authemtication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authemtication.getPrincipal();
        List<JobVacancyResponseDTO> jobVacancyResponseDTOList;
        jobVacancyResponseDTOList =  employeeJobVacancyService.seeAllJobVacancyForEmployee(employee.getID());
        return ResponseEntity.ok(jobVacancyResponseDTOList);
    }



    @PostMapping("/subscribeToJob")
    public ResponseEntity<JobVacancySubscribeDTO> subscribeToJob(@RequestBody JobVacancySubscribeDTO jobVacancySubscribeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeJobVacancyService.subscribeToJobVacancy(jobVacancySubscribeDTO.jobVacancyId(), employee.getID());
        return ResponseEntity.ok(new JobVacancySubscribeDTO(jobVacancySubscribeDTO.jobVacancyId(), employee.getID()));
    }

    @GetMapping("/see")
    public ResponseEntity<EmployeeResponseDTO> seeEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        EmployeeResponseDTO employeeResponseDTO = employeeService.findEmployeeById(employee.getID());
        return ResponseEntity.ok(employeeResponseDTO);
    }


}
