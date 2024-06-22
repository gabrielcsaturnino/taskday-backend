package com.example.taskday.domain.employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;

import java.time.LocalDate;
import java.util.List;

public record EmployeeResponseDTO(String firstName, String lastName, String email, String phoneNumber, String password, String cpf, List<String> experienceList, String city, String state, String cep, String addressStreet, String addressComplement, String addressNumber, String address, LocalDate dateOfBirth, List<EmployeeJobVacancy> registeredJob) {

}
