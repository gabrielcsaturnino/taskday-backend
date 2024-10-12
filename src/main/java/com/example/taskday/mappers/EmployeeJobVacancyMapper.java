package com.example.taskday.mappers;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancyResponseDTO;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;

public class EmployeeJobVacancyMapper {

        public static JobVacancyResponseDTO listJobVacancy(JobVacancy jobVacancy) {
            JobVacancyResponseDTO jobVacancyResponseDTO = JobVacancyResponseDTO.listToEmployee(
                    jobVacancy.getId(),
                    jobVacancy.getCompany().getName(),
                    jobVacancy.getTotalHoursJob(),
                    jobVacancy.getTitle(),
                    jobVacancy.getDescription(),
                    jobVacancy.getDesiredExperience(),
                    jobVacancy.getDayValue(),
                    jobVacancy.getJobDate(),
                    jobVacancy.getCity(),
                    jobVacancy.getStatus()
            );
            return jobVacancyResponseDTO;
        }

    public static EmployeeJobVacancyResponseDTO listEmployee(Employee employee, EmployeeJobVacancy employeeJobVacancy){
        EmployeeJobVacancyResponseDTO employeeJobVacancyResponseDTO = new EmployeeJobVacancyResponseDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber(),
                employee.getExperienceList(),
                employee.getCity(),
                employee.getState(),
                employeeJobVacancy.getPoint()
        );
        return employeeJobVacancyResponseDTO;
    }

}
