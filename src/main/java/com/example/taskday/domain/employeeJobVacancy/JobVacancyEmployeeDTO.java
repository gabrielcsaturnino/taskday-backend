package com.example.taskday.domain.employeeJobVacancy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record JobVacancyEmployeeDTO(UUID employeeId, String firstName, String lastName, String email, String phoneNumber, String cpf, List<String> experienceList, String city, String state, String postalCode, String addressStreet, String addressComplement, String addressNumber, String address, LocalDate dateOfBirth) {
}
