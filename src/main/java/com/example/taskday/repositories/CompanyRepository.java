package com.example.taskday.repositories;

import com.example.taskday.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
