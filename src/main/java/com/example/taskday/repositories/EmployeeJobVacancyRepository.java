package com.example.taskday.repositories;

import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeJobVacancyRepository extends JpaRepository<EmployeeJobVacancy, Long> {
}
