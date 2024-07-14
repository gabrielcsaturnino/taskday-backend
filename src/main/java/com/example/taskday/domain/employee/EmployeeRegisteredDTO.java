package com.example.taskday.domain.employee;

import java.util.List;

public record EmployeeRegisteredDTO(String firstName, String lastName, String email, String phoneNumber, List<String> experienceList, String city, String state, Double points) {
}
