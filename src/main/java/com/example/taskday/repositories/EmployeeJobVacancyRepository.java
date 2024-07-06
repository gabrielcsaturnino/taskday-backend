package com.example.taskday.repositories;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeJobVacancyRepository extends JpaRepository<EmployeeJobVacancy, UUID> {
    List<EmployeeJobVacancy> findByEmployee_Id(UUID employeeId);
    List<EmployeeJobVacancy> findByJobVacancy_Id(UUID jobVacancyId);
    boolean existsByEmployeeIdAndJobVacancyId(UUID employeeId, UUID jobVacancyId);
    Optional<EmployeeJobVacancy> findByEmployeeAndJobVacancy(Employee employee, JobVacancy jobVacancy);
    Optional<EmployeeJobVacancy> findByJobVacancy(JobVacancy jobVacancy);

}
