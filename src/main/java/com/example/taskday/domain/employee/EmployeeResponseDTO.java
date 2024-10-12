package com.example.taskday.domain.employee;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record EmployeeResponseDTO(
        Optional<UUID> uuid,
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<String> email,
        Optional<String> phoneNumber,
        Optional<List<String>> experienceList,
        Optional<String> city,
        Optional<String> state,
        Optional<String> postalCode,
        Optional<String> addressStreet,
        Optional<String> addressComplement,
        Optional<String> addressNumber,
        Optional<String> address,
        Optional<LocalDate> dateOfBirth
) {
    public static EmployeeResponseDTO fullEmployee(UUID uuid, String firstName, String lastName, String email,
                                                   String phoneNumber, List<String> experienceList, String city, String state,
                                                   String postalCode, String addressStreet, String addressComplement, String addressNumber,
                                                   String address, LocalDate dateOfBirth
                                            ){
        return new EmployeeResponseDTO(Optional.of(uuid), Optional.of(firstName),Optional.of(lastName),
                Optional.of(email),Optional.of(phoneNumber),Optional.of(experienceList),Optional.of(city),
                Optional.of(state),Optional.of(postalCode),Optional.of(addressStreet),Optional.of(addressComplement),
                Optional.of(addressNumber),Optional.of(address),Optional.of(dateOfBirth));
    }

   public static EmployeeResponseDTO partialEmployee(String firstName, String lastName, String phoneNumber,
                                                     List<String> experienceList, String city, String state, String address, LocalDate dateOfBirth){


       return new EmployeeResponseDTO(Optional.empty(), Optional.of(firstName),Optional.of(lastName),
               Optional.empty(),Optional.of(phoneNumber),Optional.of(experienceList),Optional.of(city),
               Optional.of(state),Optional.empty(),Optional.empty(),Optional.empty(),
               Optional.empty(),Optional.of(address),Optional.of(dateOfBirth));
    }

}
