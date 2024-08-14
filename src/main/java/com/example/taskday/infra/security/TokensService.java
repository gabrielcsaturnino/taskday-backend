package com.example.taskday.infra.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employee.Employee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokensService {

    @Value("${api.security.token.secret}")
    private String secret;

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

    public Instant getExpiryDate() {
      return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    }
}
