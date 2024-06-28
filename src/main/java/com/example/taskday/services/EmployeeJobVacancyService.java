package com.example.taskday.services;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRequestDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeJobVacancyService {
    @Autowired
    EmployeeJobVacancyRepository employeeJobVacancyRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    JobVacancyRepository jobVacancyRepository;



    public void subscribeToJobVacancy(UUID jobVacancyId, UUID employeeId){

        Optional<JobVacancy> optionalJobVacancy = jobVacancyRepository.findById(jobVacancyId);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (!optionalJobVacancy.isPresent()) {
            throw new RuntimeException("Job vacancy not found!");
        }

        if (!optionalEmployee.isPresent()) {
            throw new RuntimeException("Employee not found!");
        }

        JobVacancy jobVacancy = optionalJobVacancy.get();
        Employee employee = optionalEmployee.get();

        if (employee.getRegisteredJob().contains(jobVacancyId)) {
            throw new RuntimeException("Employee already registered!");
        }

        if (jobVacancy.getRegisteredEmployeeIds().contains(employeeId)) {
            throw new RuntimeException("Employee already registered!");
        }

        employee.getRegisteredJob().add(jobVacancyId);
        jobVacancy.getRegisteredEmployeeIds().add(employeeId);
        jobVacancyRepository.save(jobVacancy);
        employeeRepository.save(employee);

    }

    public void unsubscribeFromJobVacancy(UUID jobVacancyId, UUID employeeId){
        Optional<JobVacancy> optionalJobVacancy = jobVacancyRepository.findById(jobVacancyId);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (!optionalJobVacancy.isPresent()) {
            throw new RuntimeException("Job vacancy not found!");
        }

        if (!optionalEmployee.isPresent()) {
            throw new RuntimeException("Employee not found!");
        }

        JobVacancy jobVacancy = optionalJobVacancy.get();
        Employee employee = optionalEmployee.get();

        jobVacancy.getRegisteredEmployees().remove(employee);
        employee.getRegisteredJob().remove(jobVacancyId);
        jobVacancyRepository.save(jobVacancy);
        employeeRepository.save(employee);

    }


    public List<JobVacancyResponseDTO> seeAllJobVacancyForEmployee(UUID employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            throw new RuntimeException("Employee not found!");
        }
        Employee employee = optionalEmployee.get();
        List<JobVacancy> jobVacancyList = jobVacancyRepository.findAllById(employee.getRegisteredJob());
        return jobVacancyList.stream().map(this::convertToJobVacancyResponseDTO).collect(Collectors.toList());
    }

    public JobVacancyResponseDTO convertToJobVacancyResponseDTO(JobVacancy jobVacancy) {
        return new JobVacancyResponseDTO(
                jobVacancy.getId(),
                jobVacancy.getTotalHoursJob(),
                jobVacancy.getTitle(),
                jobVacancy.getDescription(),
                jobVacancy.getDesiredExperience(),
                jobVacancy.getDayValue(),
                jobVacancy.getStatus(),
                jobVacancy.getJobDate(),
                jobVacancy.getCompany().getId(),
                jobVacancy.getRegisteredEmployees(),
                jobVacancy.getRegisteredEmployeeIds()

        );
    }






}
