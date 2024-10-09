package com.example.taskday.domain.employee;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;


public record EmployeeLoginResponseDTO(String token, Collection<? extends GrantedAuthority> authorities) {
}
