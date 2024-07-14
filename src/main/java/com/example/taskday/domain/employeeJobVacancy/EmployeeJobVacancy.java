package com.example.taskday.domain.employeeJobVacancy;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity @Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class EmployeeJobVacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    private JobVacancy jobVacancy;

    private double pont;

    public EmployeeJobVacancy(Employee employee, JobVacancy jobVacancy) {
        this.employee = employee;
        this.jobVacancy = jobVacancy;
    }


}
