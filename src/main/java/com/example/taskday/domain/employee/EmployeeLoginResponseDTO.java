package com.example.taskday.domain.employee;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;


public record EmployeeLoginResponseDTO(String token, Collection<? extends GrantedAuthority> authorities) {
}
