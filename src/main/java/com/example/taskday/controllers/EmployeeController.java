package com.example.taskday.controllers;


import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeChangeAccountRequestDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.domain.employee.PasswordChangeRequestDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.domain.jobVacancy.JobVacancySubscribeRequestDTO;
import com.example.taskday.services.EmailService;
import com.example.taskday.services.EmployeeJobVacancyService;
import com.example.taskday.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {


    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeJobVacancyService employeeJobVacancyService;

    @GetMapping("/jobs/subscriptions")
    public ResponseEntity<List<JobVacancyResponseDTO>> getAllSubscribedJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        List<JobVacancyResponseDTO> jobVacancies = employeeJobVacancyService.getAllJobVacancyForEmployee(employee.getId());
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
    public ResponseEntity<?> subscribeToJob(@RequestBody @Valid JobVacancySubscribeRequestDTO jobVacancySubscribeRequestDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeJobVacancyService.subscribeToJobVacancy(jobVacancySubscribeRequestDTO.jobVacancyId(), employee.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/account")
    public ResponseEntity<?> updateAccount(@RequestBody @Valid EmployeeChangeAccountRequestDTO employeeChangeAccountRequestDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeService.changeAccount(employeeChangeAccountRequestDTO, employee);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/password")
    public void updatePassword(@RequestBody @Valid PasswordChangeRequestDTO passwordChangeRequestDTO) throws OperationException {
        if (passwordChangeRequestDTO.password().length() <= 10) {
            throw new OperationException("A senha deve ter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(passwordChangeRequestDTO.password());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeService.changePassword(encryptedPassword, employee, passwordChangeRequestDTO.code());
    }

    @PostMapping("/request-password-change")
    public ResponseEntity<?> requestPasswordChange() throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeService.resendConfirmationCode(employee.getEmail());
        return ResponseEntity.ok("Código enviado!");
    }



    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeProfile() throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        EmployeeResponseDTO employeeResponseDTO = employeeService.findEmployee(employee);
        return ResponseEntity.ok().body(employeeResponseDTO);
    }

    @GetMapping()
    public ResponseEntity<EmployeeResponseDTO> getPartialProfile(@RequestParam @Valid UUID employeeId) throws OperationException {
        EmployeeResponseDTO employeeResponseDTO = employeeService.findPartialEmployee(employeeId);
        return ResponseEntity.ok().body(employeeResponseDTO);
    }


}
