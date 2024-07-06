package com.example.taskday.domain.company;

import com.example.taskday.domain.jobVacancy.JobVacancy;

import java.util.List;
import java.util.UUID;

public record CompanyResponseDTO(UUID id, String name, String cnpj, String addressStreet, String addressComplement, String addressNumber, String address, String city, String state, String postalCode, String email, String phoneNumber, String ownerName, List<CompanyJobVacancyListDTO> jobList) {
}
