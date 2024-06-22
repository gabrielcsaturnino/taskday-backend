package com.example.taskday.domain.employeeJobVacancy;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import jakarta.persistence.*;

@Entity
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

}
