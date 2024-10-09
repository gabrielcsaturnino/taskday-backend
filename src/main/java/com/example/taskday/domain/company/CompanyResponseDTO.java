package com.example.taskday.domain.company;

import java.util.Optional;
import java.util.UUID;

public record CompanyResponseDTO(Optional<UUID> uuid, Optional<String> name, Optional<String> cnpj, Optional<String> addressStreet,
                                 Optional<String> addressComplement, Optional<String> addressNumber, Optional<String> address,
                                 Optional<String> city, Optional<String> state, Optional<String> postalCode, Optional<String> email, Optional<String> phoneNumber, Optional<String> ownerName) {


   public static CompanyResponseDTO partialCompany(UUID uuid, String name, String address, String city, String state){
       return new CompanyResponseDTO (Optional.of(uuid), Optional.of(name), Optional.empty(),
               Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(address),
               Optional.of(city), Optional.of(state), Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty());
   }


    public static CompanyResponseDTO completeCompany(UUID uuid, String name, String addressStreet,String addressComplemet,
                                                     String addressNumber,String address,
                                                     String city, String state, String postalCode, String email, String phoneNumber, String ownerName){
        return new CompanyResponseDTO (Optional.of(uuid), Optional.of(name), Optional.empty(),
                Optional.of(addressStreet), Optional.of(addressComplemet), Optional.of(addressNumber), Optional.of(address),
                Optional.of(city), Optional.of(state), Optional.of(postalCode),Optional.of(email),Optional.of(phoneNumber), Optional.of(ownerName));
    }


}
