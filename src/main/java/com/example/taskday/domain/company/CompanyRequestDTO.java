package com.example.taskday.domain.company;

import com.example.taskday.domain.jobVacancy.JobVacancy;

import java.util.List;

public record CompanyRequestDTO(String name, String cnpj, String addressStreet, String addressComplement, String addressNumber, String address, String city, String state, String postalCode, String password, String email, String phoneNumber, String ownerName, List<JobVacancy> jobList) {
}
