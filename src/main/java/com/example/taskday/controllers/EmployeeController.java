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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public ResponseEntity<List<EmployeeJobVacancyDTO>> getAllSubscribeJob() {
        Authentication authemtication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authemtication.getPrincipal();
        List<EmployeeJobVacancyDTO> jobVacancyResponseDTOList;
        jobVacancyResponseDTOList =  employeeJobVacancyService.getAllJobVacancyForEmployee(employee.getId());
        return ResponseEntity.ok(jobVacancyResponseDTOList);
    }


   @DeleteMapping("/unsubscribeToJobVacancy")
   public ResponseEntity<List<EmployeeJobVacancyDTO>> unsubscribeToJobVacancy(@RequestParam("jobVacancyId") UUID jobId) throws OperationException {
        Authentication authemtication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authemtication.getPrincipal();
        employeeJobVacancyService.unsubscribeFromJobVacancy(jobId,employee.getId());
        List<EmployeeJobVacancyDTO> jobVacancyResponseDTOList;
        jobVacancyResponseDTOList = employeeJobVacancyService.getAllJobVacancyForEmployee(employee.getId());
        return ResponseEntity.ok(jobVacancyResponseDTOList);
   }


    @PostMapping("/subscribeToJob")
    public ResponseEntity<JobVacancySubscribeDTO> subscribeToJobVacancy(@RequestBody JobVacancySubscribeDTO jobVacancySubscribeDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeJobVacancyService.subscribeToJobVacancy(jobVacancySubscribeDTO.jobVacancyId(), employee.getId());
        double points = employeeJobVacancyService.getPoints(jobVacancySubscribeDTO.jobVacancyId(), employee.getId());
        return ResponseEntity.ok(new JobVacancySubscribeDTO(jobVacancySubscribeDTO.jobVacancyId(), employee.getId(), points));
    }

    @PostMapping("/changeAccount")
    public void changeAccount(@RequestBody @Valid EmployeeChangeAccountDTO employeeChangeAccountDTO) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        employeeService.changeAccount(employeeChangeAccountDTO, employee);
    }

    @PostMapping("/changePassword")
    public void changePassword(@RequestBody @Valid String password) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        if(password.length() <= 10){
            throw new OperationException("Chave deve conter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        employeeService.changePassword(encryptedPassword, employee);
    }



    @GetMapping("/seeAllEmployee")
    public ResponseEntity<EmployeeResponseDTO> seeEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        EmployeeResponseDTO employeeResponseDTO = employeeService.findEmployeeById(employee.getId());
        return ResponseEntity.ok(employeeResponseDTO);
    }
}
