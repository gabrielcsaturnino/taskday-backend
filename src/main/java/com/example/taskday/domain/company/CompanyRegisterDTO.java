package com.example.taskday.domain.company;

public record CompanyRegisterDTO(String name, String cnpj, String addressStreet, String addressComplement, String addressNumber, String address, String city, String state, String postalCode, String password, String email, String phoneNumber, String ownerName) {
}
