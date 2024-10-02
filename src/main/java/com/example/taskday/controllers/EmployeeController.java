package com.example.taskday.controllers;


import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeChangeAccountDTO;
import com.example.taskday.domain.employee.EmployeeRegisterDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancyDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancySubscribeDTO;
import com.example.taskday.services.EmployeeJobVacancyService;
import com.example.taskday.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeJobVacancyService employeeJobVacancyService;

    @GetMapping("/jobs/subscriptions")
    public ResponseEntity<List<EmployeeJobVacancyDTO>> getAllSubscribedJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        List<EmployeeJobVacancyDTO> jobVacancies = employeeJobVacancyService.getAllJobVacancyForEmployee(employee.getId());
        return ResponseEntity.ok(jobVacancies);
    }

    @DeleteMapping("/jobs/{jobVacancyId}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable UUID jobVacancyId) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeJobVacancyService.unsubscribe(jobVacancyId, employee.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/jobs/subscribe")
    public ResponseEntity<JobVacancySubscribeDTO> subscribeToJob(@RequestBody @Valid JobVacancySubscribeDTO jobVacancySubscribeDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeJobVacancyService.subscribeToJobVacancy(jobVacancySubscribeDTO.jobVacancyId(), employee.getId());
        double points = employeeJobVacancyService.getPoints(jobVacancySubscribeDTO.jobVacancyId(), employee.getId());
        return ResponseEntity.ok(new JobVacancySubscribeDTO(jobVacancySubscribeDTO.jobVacancyId(), employee.getId(), points));
    }

    @PutMapping("/account")
    public void updateAccount(@RequestBody @Valid EmployeeChangeAccountDTO employeeChangeAccountDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeService.changeAccount(employeeChangeAccountDTO, employee);
    }


    @PutMapping("/password")
    public void updatePassword(@RequestBody @Valid String password) throws OperationException {
        if (password.length() <= 10) {
            throw new OperationException("A senha deve ter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeService.changePassword(encryptedPassword, employee);
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeProfile() throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        EmployeeResponseDTO employeeResponseDTO = employeeService.findEmployeeById(employee.getId());
        return ResponseEntity.ok(employeeResponseDTO);

    }
}
