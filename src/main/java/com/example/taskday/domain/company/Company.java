package com.example.taskday.domain.company;


import com.example.taskday.domain.jobVacancy.JobVacancy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Table(name = "company") @Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String cnpj;
    private String addressStreet;
    private String addressComplement;
    private String addressNumber;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String password;
    private String email;
    private String phoneNumber;
    private String ownerName;

    @OneToMany(mappedBy = "company" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobVacancy> jobList = new ArrayList<>();

    public Company(String name, String cnpj, String addressStreet, String addressComplement, String addressNumber, String address, String city, String state, String postalCode, String password, String email, String phoneNumber, String ownerName, List<JobVacancy> jobList) {
        this.name = name;
        this.cnpj = cnpj;
        this.addressStreet = addressStreet;
        this.addressComplement = addressComplement;
        this.addressNumber = addressNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ownerName = ownerName;
        this.jobList = jobList;
    }
}
