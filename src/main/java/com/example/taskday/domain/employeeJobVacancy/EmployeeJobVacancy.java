package com.example.taskday.domain.employeeJobVacancy;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Setter @Getter @NoArgsConstructor
public class EmployeeJobVacancy {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "job_vacancy_id")
    private JobVacancy jobVacancy;

    public EmployeeJobVacancy(Employee employee, JobVacancy jobVacancy) {
        this.employee = employee;
        this.jobVacancy = jobVacancy;
    }
}
