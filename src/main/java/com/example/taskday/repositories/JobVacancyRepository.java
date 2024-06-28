package com.example.taskday.repositories;

import com.example.taskday.domain.jobVacancy.JobVacancy;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMappings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobVacancyRepository extends JpaRepository<JobVacancy, UUID> {
    List<JobVacancy> findByCompany_Id(UUID companyId);


}
