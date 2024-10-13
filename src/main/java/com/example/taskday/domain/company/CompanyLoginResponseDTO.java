package com.example.taskday.domain.company;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record CompanyLoginResponseDTO(String token, String refreshToken,Collection<String> authorities) {
}
