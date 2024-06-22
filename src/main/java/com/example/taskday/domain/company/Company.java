package com.example.taskday.domain.company;


import com.example.taskday.domain.jobVacancy.JobVacancy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Table(name = "company") @Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String email;

    @OneToMany(mappedBy = "company" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobVacancy> jobList = new ArrayList<>();



}
