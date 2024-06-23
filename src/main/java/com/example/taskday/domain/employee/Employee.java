package com.example.taskday.domain.employee;

import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity @Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String cpf;
    @ElementCollection
    private List<String> experienceList;
    private String city;
    private String state;
    private String postalCode;
    private String addressStreet;
    private String addressComplement;
    private String addressNumber;
    private String address;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeJobVacancy> registeredJob = new ArrayList<>();





    public Employee(String firstName, String lastName, String email, String phoneNumber, String password, String cpf, List<String> experienceList, String city, String state, String postalCode, String addressStreet, String addressComplement, String addressNumber, String address, LocalDate dateOfBirth, @Nullable List<EmployeeJobVacancy> registeredJob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.cpf = cpf;
        this.experienceList = experienceList;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.addressStreet = addressStreet;
        this.addressComplement = addressComplement;
        this.addressNumber = addressNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.registeredJob = registeredJob;
    }

}
