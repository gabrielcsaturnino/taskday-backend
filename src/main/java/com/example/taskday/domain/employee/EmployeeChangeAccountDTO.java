package com.example.taskday.domain.employee;

import java.util.List;

public record EmployeeChangeAccountDTO(String firstName, String lastName, String email, String phoneNumber, String city, String state, List experienceList, String address, String addressStreet, String addressComplement, String addressNumber, String postalCode ) {
}
