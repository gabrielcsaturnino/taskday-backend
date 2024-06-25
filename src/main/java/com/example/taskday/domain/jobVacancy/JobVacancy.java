package com.example.taskday.domain.jobVacancy;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.company.Company;
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

@Entity @Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jobVacancy")
public class JobVacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    @ElementCollection
    private List<String> desiredExperience = new ArrayList<>();
    private Double dayValue;
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate jobDate;

    private int totalHoursJob;

    @OneToMany
    private List<Employee> registeredEmployees = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "company_id")
    @Nullable
    private Company company;

    public JobVacancy(int totalHoursJob, String title, String description, List<String> desiredExperience, Double dayValue, String status, LocalDate jobDate) {
        this.totalHoursJob = totalHoursJob;
        this.title = title;
        this.description = description;
        this.desiredExperience = desiredExperience;
        this.dayValue = dayValue;
        this.status = status;
        this.jobDate = jobDate;

    }


}
