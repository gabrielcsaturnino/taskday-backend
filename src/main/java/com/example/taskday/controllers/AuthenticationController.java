package com.example.taskday.controllers;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyAuthenticationDTO;
import com.example.taskday.domain.company.CompanyLoginResponseDTO;
import com.example.taskday.domain.company.CompanyRegisterDTO;
import com.example.taskday.domain.employee.*;
import com.example.taskday.infra.security.TokensService;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.services.CompanyService;
import com.example.taskday.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
     private AuthenticationManager authenticationManager;

    @Autowired
     private EmployeeRepository employeeRepository;

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
        return ResponseEntity.ok(new EmployeeLoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated EmployeeRegisterDTO employeeRegisterDTO) {
          if(employeeRepository.findByEmail(employeeRegisterDTO.email()) != null){
              return ResponseEntity.badRequest().build();
          }

          String encryptedPassword = new BCryptPasswordEncoder().encode(employeeRegisterDTO.password());
          employeeService.createEmployee(employeeRegisterDTO, encryptedPassword);
          return ResponseEntity.ok(new CompanyLoginResponseDTO());
    }


    @PostMapping("/register/company")
    public ResponseEntity companyRegister(@RequestBody @Validated CompanyRegisterDTO companyRegisterDTO) {
        if(employeeRepository.findByEmail(companyRegisterDTO.email()) != null){
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(companyRegisterDTO.password());
        companyService.createCompany(companyRegisterDTO, encryptedPassword);
        return ResponseEntity.ok(new CompanyLoginResponseDTO());
    }

    @PostMapping("/login/company")
    public ResponseEntity companyLogin(@RequestBody @Validated CompanyAuthenticationDTO companyAuthenticationDTO ) {


        var usernamePassword = new UsernamePasswordAuthenticationToken(companyAuthenticationDTO.email(), companyAuthenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokensService.generateCompanyToken((Company) auth.getPrincipal());
        return ResponseEntity.ok(new EmployeeLoginResponseDTO(token));
    }


}
