package com.example.taskday.controllers;


import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyAuthenticationDTO;
import com.example.taskday.domain.company.CompanyLoginResponseDTO;
import com.example.taskday.domain.company.CompanyRegisterDTO;
import com.example.taskday.domain.employee.*;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.infra.security.TokensService;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.services.CompanyService;
import com.example.taskday.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;


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
    public ResponseEntity<EmployeeLoginResponseDTO> loginEmployee(@RequestBody @Valid EmployeeAuthenticationDTO employeeAuthenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(employeeAuthenticationDTO.email(), employeeAuthenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokensService.generateEmployeeToken((Employee) auth.getPrincipal());
        return ResponseEntity.ok(new EmployeeLoginResponseDTO(token, auth.getAuthorities()));
    }

    @PostMapping("/login/company")
    public ResponseEntity<CompanyLoginResponseDTO> loginCompany(@RequestBody @Valid CompanyAuthenticationDTO companyAuthenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(companyAuthenticationDTO.email(), companyAuthenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokensService.generateCompanyToken((Company) auth.getPrincipal());
        return ResponseEntity.ok(new CompanyLoginResponseDTO(token, auth.getAuthorities()));
    }

    @PostMapping("/register/employee")
    public ResponseEntity<Void> registerEmployee(@RequestBody @Valid EmployeeRegisterDTO employeeRegisterDTO) throws OperationException {
        if (employeeRegisterDTO.password().length() <= 10) {
            throw new OperationException("A senha deve ter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(employeeRegisterDTO.password());
        employeeService.createEmployee(employeeRegisterDTO, encryptedPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/company")
    public ResponseEntity<Void> registerCompany(@RequestBody @Valid CompanyRegisterDTO companyRegisterDTO) throws OperationException {
        if (companyRegisterDTO.password().length() <= 10) {
            throw new OperationException("A senha deve ter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(companyRegisterDTO.password());
        companyService.createCompany(companyRegisterDTO, encryptedPassword);
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

