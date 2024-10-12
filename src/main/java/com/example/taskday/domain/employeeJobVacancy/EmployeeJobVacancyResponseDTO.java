package com.example.taskday.domain.employeeJobVacancy;
import java.util.List;
import java.util.UUID;

public record EmployeeJobVacancyResponseDTO(UUID uuid, String firstName, String lastName,
                                            String phoneNumber, List<String> experienceList,
                                            String city, String state, Double point) {

}
