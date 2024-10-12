package com.example.taskday.controllers;


import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyAuthenticationRequestDTO;
import com.example.taskday.domain.company.CompanyLoginResponseDTO;
import com.example.taskday.domain.company.CompanyCreateRequestDTO;
import com.example.taskday.domain.employee.*;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.infra.security.TokensService;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.services.CompanyService;
import com.example.taskday.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TokensService tokensService;

    @Autowired
    private CompanyService companyService;

    @PostMapping("/login/employee")
    public ResponseEntity<EmployeeLoginResponseDTO> loginEmployee(@RequestBody @Valid EmployeeAuthenticationRequestDTO employeeAuthenticationRequestDTO) throws OperationException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(employeeAuthenticationRequestDTO.email(), employeeAuthenticationRequestDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokensService.generateEmployeeToken((Employee) auth.getPrincipal());


        return ResponseEntity.ok(new EmployeeLoginResponseDTO(token, auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())));
    }

    @PostMapping("/login/company")
    public ResponseEntity<CompanyLoginResponseDTO> loginCompany(@RequestBody @Valid CompanyAuthenticationRequestDTO companyAuthenticationRequestDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(companyAuthenticationRequestDTO.email(), companyAuthenticationRequestDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokensService.generateCompanyToken((Company) auth.getPrincipal());
        return ResponseEntity.ok(new CompanyLoginResponseDTO(token, auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())));
    }

    @PostMapping("/register/employee")
    public ResponseEntity<String> registerEmployee(@RequestBody @Valid EmployeeCreateRequestDTO employeeCreateRequestDTO) throws OperationException {
        if (employeeCreateRequestDTO.password().length() <= 10) {
            throw new OperationException("A senha deve ter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(employeeCreateRequestDTO.password());
        employeeService.createEmployee(employeeCreateRequestDTO, encryptedPassword);

        var usernamePassword = new UsernamePasswordAuthenticationToken(employeeCreateRequestDTO.email(), employeeCreateRequestDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokensService.generateEmployeeToken((Employee) auth.getPrincipal());


        ((Employee) auth.getPrincipal()).setEnabled(false);
        employeeRepository.save((Employee) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register/company")
    public ResponseEntity registerCompany(@RequestBody @Valid CompanyCreateRequestDTO companyCreateRequestDTO) throws OperationException {
        if (companyCreateRequestDTO.password().length() <= 10) {
            throw new OperationException("A senha deve ter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(companyCreateRequestDTO.password());
        companyService.createCompany(companyCreateRequestDTO, encryptedPassword);

        var usernamePassword = new UsernamePasswordAuthenticationToken(companyCreateRequestDTO.email(), companyCreateRequestDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokensService.generateCompanyToken((Company) auth.getPrincipal());

        ((Company) auth.getPrincipal()).setEnabled(false);
        companyRepository.save((Company) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }


    @PostMapping("/confirmcode")
    public ResponseEntity confirmCode(@RequestParam String code) throws OperationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if(principal instanceof Employee){
            Employee employee = (Employee) authentication.getPrincipal();
            employeeService.confirmationAccount(employee, code);
            return ResponseEntity.ok().build();
        }

        if(principal instanceof Company){
            Company company = (Company) authentication.getPrincipal();
            companyService.confirmationAccount(company, code);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();

    }

    @PostMapping("/resendcode")
    public ResponseEntity resendCode(@RequestParam String email) throws OperationException {
          employeeService.resendConfirmationCode(email);
          return ResponseEntity.ok().build();
    }



    @GetMapping("/role/company")
    public ResponseEntity<Boolean> isCompany(Authentication authentication) {
        return ResponseEntity.ok(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_COMPANY")));
    }

    @GetMapping("/role/employee")
    public ResponseEntity<Boolean> isEmployee(Authentication authentication) {
        return ResponseEntity.ok(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_EMPLOYEE")));
    }
}

