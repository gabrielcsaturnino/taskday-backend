package com.example.taskday.repositories;

import com.example.taskday.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    UserDetails findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    Optional<Company> findByName(String name);
}
