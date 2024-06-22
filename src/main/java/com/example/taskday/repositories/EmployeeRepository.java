package com.example.taskday.repositories;

import com.example.taskday.domain.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
