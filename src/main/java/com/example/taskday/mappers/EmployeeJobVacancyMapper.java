package com.example.taskday.mappers;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancyDTO;
import com.example.taskday.domain.employeeJobVacancy.JobVacancyEmployeeDTO;
import com.example.taskday.domain.jobVacancy.JobVacancy;

public class EmployeeJobVacancyMapper {
    public static JobVacancyEmployeeDTO listEmployeeToJobVacancyResponseDTO(Employee employee) {
        JobVacancyEmployeeDTO jobVacancyEmployeeDTO = new JobVacancyEmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getCpf(),
                employee.getExperienceList(),
                employee.getCity(),
                employee.getState(),
                employee.getPostalCode(),
                employee.getAddressStreet(),
                employee.getAddressComplement(),
                employee.getAddressNumber(),
                employee.getAddress(),
                employee.getDateOfBirth()
        );
     return jobVacancyEmployeeDTO;
    }

    public static EmployeeJobVacancyDTO listJobVacancyToEmployeeDTO(JobVacancy jobVacancy) {
        EmployeeJobVacancyDTO employeeJobVacancyDTO = new EmployeeJobVacancyDTO(
                jobVacancy.getId(),
                jobVacancy.getTotalHoursJob(),
                jobVacancy.getTitle(),
                jobVacancy.getDescription()
        );

        return employeeJobVacancyDTO;
    }
}
