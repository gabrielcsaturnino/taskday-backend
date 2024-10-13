package com.example.taskday.infra.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.Employee;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokensService {


    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    @Value("${api.security.token.secret}")
    private String secret;

    public TokensService(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    public String generateEmployeeToken(Employee employee) {

        try{
           Algorithm algorithm = Algorithm.HMAC256(secret);
           String token = JWT.create()
                   .withIssuer("taskday")
                   .withSubject(employee.getEmail())
                   .withExpiresAt(getExpiryDate())
                   .sign(algorithm);
           return token;
       }catch (JWTCreationException exception){
            throw new RuntimeException("Error generating token", exception);
        }
    }

    public String generateEmployeeTokenRefresh(Employee employee){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("taskday")
                    .withSubject(employee.getEmail())
                    .withExpiresAt(getExpiryDateRefresh())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error generating token", exception);
        }
    }

    public String generateCompanyTokenRefresh(Company company){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("taskday")
                    .withSubject(company.getEmail())
                    .withExpiresAt(getExpiryDateRefresh())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error generating token", exception);
        }
    }

    public String generateCompanyToken(Company company) {

        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("taskday")
                    .withSubject(company.getEmail())
                    .withExpiresAt(getExpiryDate())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error generating token", exception);
        }
    }



    public String verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("taskday")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            return "";
        }
    }

    public String validateRefreshTokenEmployee(String refreshToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            var verifier = JWT.require(algorithm)
                    .withIssuer("taskday")
                    .build();

            var decodedJWT = verifier.verify(refreshToken);

            String email = decodedJWT.getSubject();
            Employee employee = (Employee) employeeRepository.findByEmail(email);

            String newAccessToken = generateEmployeeToken(employee);
            return newAccessToken;

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Refresh token inválido ou expirado.", exception);
        }
    }


    public String validateRefreshTokenCompany(String refreshToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            var verifier = JWT.require(algorithm)
                    .withIssuer("taskday")
                    .build();

            var decodedJWT = verifier.verify(refreshToken);

            String email = decodedJWT.getSubject();
            Company company = (Company) companyRepository.findByEmail(email);

            String newAccessToken = generateCompanyToken(company);
            return newAccessToken;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Refresh token inválido ou expirado.", exception);
        }
    }



    public Instant getExpiryDateRefresh(){
        return LocalDateTime.now().plusMonths(1).toInstant(ZoneOffset.of("-03:00"));

    }

    public Instant getExpiryDate() {
      return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    }
}
