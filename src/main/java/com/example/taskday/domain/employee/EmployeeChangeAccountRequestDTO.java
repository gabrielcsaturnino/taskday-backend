package com.example.taskday.domain.employee;

import java.util.List;
import java.util.Optional;

public record EmployeeChangeAccountRequestDTO(Optional<String> firstName,
                                              Optional<String> lastName,
                                              Optional<String> email,
                                              Optional<String> phoneNumber,
                                              Optional<String> city,
                                              Optional<String> state,
                                              Optional<List<String>> experienceList,
                                              Optional<String> address,
                                              Optional<String> addressStreet,
                                              Optional<String> addressComplement,
                                              Optional<String> addressNumber,
                                              Optional<String> postalCode) {
}
