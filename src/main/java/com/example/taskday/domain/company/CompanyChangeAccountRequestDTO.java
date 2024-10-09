package com.example.taskday.domain.company;

import java.util.Optional;

public record CompanyChangeAccountRequestDTO(Optional<String> name, Optional<String> addressStreet, Optional<String> addressComplement,
                                             Optional<String> addressNumber, Optional<String> address, Optional<String> city,
                                             Optional<String> state, Optional<String> postalCode, Optional<String> email, Optional<String> phoneNumber) {
}
