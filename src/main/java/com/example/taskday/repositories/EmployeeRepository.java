package com.example.taskday.repositories;

import com.example.taskday.domain.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    UserDetails findByEmail(String email);
    boolean existsByEmail(String email);
    @Override
    List<Employee> findAll();

    boolean existsByCpf(String cpf);
}
