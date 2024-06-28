package com.example.taskday.domain.employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.enums.RoleType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record EmployeeResponseDTO(UUID employeeId, String firstName, String lastName, String email, String phoneNumber, String password, String cpf, List<String> experienceList, String city, String state, String postalCode, String addressStreet, String addressComplement, String addressNumber, String address, LocalDate dateOfBirth, List<UUID> registeredJobs) {

}
