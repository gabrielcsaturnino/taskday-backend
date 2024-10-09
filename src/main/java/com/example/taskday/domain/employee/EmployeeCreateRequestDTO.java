package com.example.taskday.domain.employee;

import java.time.LocalDate;
import java.util.List;

public record EmployeeCreateRequestDTO(String firstName, String lastName, String email, String phoneNumber, String password, String cpf, List<String> experienceList, String city, String state, String postalCode, String addressStreet, String addressComplement, String addressNumber, String address, LocalDate dateOfBirth) {

}
