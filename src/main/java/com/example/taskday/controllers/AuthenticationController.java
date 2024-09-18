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


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated EmployeeAuthenticationDTO employeeAuthenticationDTO ) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(employeeAuthenticationDTO.email(), employeeAuthenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokensService.generateEmployeeToken((Employee) auth.getPrincipal());
        return ResponseEntity.ok(new EmployeeLoginResponseDTO(token, auth.getAuthorities()));
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated EmployeeRegisterDTO employeeRegisterDTO) throws OperationException {
        if(employeeRegisterDTO.password().length() <= 10){
            throw new OperationException("Chave deve conter pelo menos 11 caracteres!");
        }
          String encryptedPassword = new BCryptPasswordEncoder().encode(employeeRegisterDTO.password());
          employeeService.createEmployee(employeeRegisterDTO, encryptedPassword);
          return ResponseEntity.ok().build();
    }


    @PostMapping("/register/company")
    public ResponseEntity companyRegister(@RequestBody @Validated CompanyRegisterDTO companyRegisterDTO) throws OperationException {
        if(companyRegisterDTO.password().length() <= 10){
            throw new OperationException("Chave deve conter pelo menos 11 caracteres!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(companyRegisterDTO.password());
        companyService.createCompany(companyRegisterDTO, encryptedPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/company")
    public ResponseEntity companyLogin(@RequestBody @Validated CompanyAuthenticationDTO companyAuthenticationDTO ) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(companyAuthenticationDTO.email(), companyAuthenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokensService.generateCompanyToken((Company) auth.getPrincipal());
        return ResponseEntity.ok(new CompanyLoginResponseDTO(token, auth.getAuthorities()));
    }

    @GetMapping("/isCompany")
    public boolean isCompany(Authentication authentication) {
        var role_type = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        if(role_type.equals("ROLE_COMPANY")){
            return true;
        }else{
            return false;
        }
    }

    @GetMapping("/isEmployee")
    public boolean isEmployee(Authentication authentication) {
        var role_type = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        if(role_type.equals("ROLE_EMPLOYEE")){
            return true;
        }else{
            return false;
        }
    }
}
