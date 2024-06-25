package com.example.taskday.domain.employee;

import com.example.taskday.domain.jobVacancy.JobVacancy;

import java.time.LocalDate;
import java.util.List;

public record EmployeeRegisterDTO(String firstName, String lastName, String email, String phoneNumber, String password, String cpf, List<String> experienceList, String city, String state, String postalCode, String addressStreet, String addressComplement, String addressNumber, String address, LocalDate dateOfBirth) {

}
