package com.example.taskday.controllers;

import br.com.caelum.stella.validation.CPFValidator;
import ch.qos.logback.core.subst.Token;
import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyAuthenticationDTO;
import com.example.taskday.domain.company.CompanyLoginResponseDTO;
import com.example.taskday.domain.company.CompanyRegisterDTO;
import com.example.taskday.domain.employee.*;
import com.example.taskday.infra.security.TokensService;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.services.CompanyService;
import com.example.taskday.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
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

        if(employeeRepository.findByEmail(employeeAuthenticationDTO.email()) == null){
            return new ResponseEntity("Email ou Senha invalida",HttpStatus.BAD_REQUEST);
        }
        var usernamePassword = new UsernamePasswordAuthenticationToken(employeeAuthenticationDTO.email(), employeeAuthenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokensService.generateEmployeeToken((Employee) auth.getPrincipal());
        return ResponseEntity.ok(new EmployeeLoginResponseDTO(token, auth.getAuthorities()));
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated EmployeeRegisterDTO employeeRegisterDTO) {
        if(employeeRepository.existsByEmail(employeeRegisterDTO.email()) || companyRepository.existsByEmail(employeeRegisterDTO.email())) {
            return new ResponseEntity ("Email already exists",HttpStatus.CONFLICT);
        }
        if(!employeeRegisterDTO.email().contains("@")){
            return new ResponseEntity ("Invalid email",HttpStatus.BAD_REQUEST);
        }

        try {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.assertValid(employeeRegisterDTO.cpf());
        }catch (Exception e){
            return new ResponseEntity ("Invalid CPF",HttpStatus.BAD_REQUEST);
        }

          String encryptedPassword = new BCryptPasswordEncoder().encode(employeeRegisterDTO.password());

          employeeService.createEmployee(employeeRegisterDTO, encryptedPassword);
          return ResponseEntity.ok().build();
    }


    @PostMapping("/register/company")
    public ResponseEntity companyRegister(@RequestBody @Validated CompanyRegisterDTO companyRegisterDTO) {
        if(employeeRepository.existsByEmail(companyRegisterDTO.email()) || companyRepository.existsByEmail(companyRegisterDTO.email())) {
            return new ResponseEntity ("Email already exists",HttpStatus.CONFLICT);
        }

        if(companyRepository.existsByName(companyRegisterDTO.name())) {
            return new ResponseEntity("Company already exists",HttpStatus.CONFLICT);
        }

        if(!companyRegisterDTO.email().contains("@")){
            return new ResponseEntity ("Invalid email",HttpStatus.BAD_REQUEST);
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
