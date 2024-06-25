package com.example.taskday.services;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRequestDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeJobVacancyService {
    @Autowired
    EmployeeJobVacancyRepository employeeJobVacancyRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    JobVacancyService jobVacancyService;

    public void createEmployeeJobVacancy(EmployeeRequestDTO employeeRequestDTO, JobVacancyRequestDTO jobVacancyRequestDTO) {
        this.addJobToEmployee(employeeRequestDTO, jobVacancyRequestDTO);
        this.addEmployeeToJobVacancy(employeeRequestDTO, jobVacancyRequestDTO);
        employeeJobVacancyRepository.save(new EmployeeJobVacancy(employeeService.convertToEmployee(employeeRequestDTO), jobVacancyService.convertToJobVacancy(jobVacancyRequestDTO)));
    }

    public void addJobToEmployee(EmployeeRequestDTO employeeRequestDTO, JobVacancyRequestDTO jobVacancyRequestDTO) {
        Employee employee = employeeService.convertToEmployee(employeeRequestDTO);
        employee.getRegisteredJob().add(jobVacancyService.convertToJobVacancy(jobVacancyRequestDTO));

    }

    public void addEmployeeToJobVacancy(EmployeeRequestDTO employeeRequestDTO, JobVacancyRequestDTO jobVacancyRequestDTO) {
        JobVacancy jobVacancy = jobVacancyService.convertToJobVacancy(jobVacancyRequestDTO);
        jobVacancy.getRegisteredEmployees().add(employeeService.convertToEmployee(employeeRequestDTO));
    }

}
