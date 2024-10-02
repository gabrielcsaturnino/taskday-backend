package com.example.taskday.domain.jobVacancy;

import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.company.Company;
import com.example.taskday.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String title;

    private String description;


    @ElementCollection(fetch = FetchType.EAGER)
    @NotNull(message = "Esse campo não pode ser nulo")
    private List<String> desiredExperience = new ArrayList<>();


    @NotNull(message = "Esse campo não pode ser nulo")
    @PositiveOrZero(message = "Esse campo deve ser maior ou igual a zero")
    private Double dayValue;


    @NotNull(message = "Esse campo não pode ser nulo")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @FutureOrPresent(message = "Selecione uma data valida!")
    private LocalDate jobDate;

    @NotNull(message = "Esse campo não pode ser nulo")
    @NegativeOrZero(message = "Esse campo deve ser maior que zero")
    private int totalHoursJob;

    @OneToMany(mappedBy = "jobVacancy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeJobVacancy> employeeJobVacancies = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String city;

    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String state;

    @NotNull(message = "Esse campo não pode ser nulo")
    private Status status;

    public JobVacancy(int totalHoursJob, String title, String description, List<String> desiredExperience, Double dayValue, LocalDate jobDate, Company company, String state, String city, Status status) {
        this.totalHoursJob = totalHoursJob;
        this.title = title;
        this.description = description;
        this.desiredExperience = desiredExperience;
        this.dayValue = dayValue;
        this.jobDate = jobDate;
        this.company = company;
        this.city = city;
        this.state = state;
        this.status = status;

    }

}
