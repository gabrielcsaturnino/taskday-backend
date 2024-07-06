package com.example.taskday.services;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancyDTO;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.mappers.EmployeeJobVacancyMapper;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.taskday.mappers.JobVacancyMapper;

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

        Optional<EmployeeJobVacancy> employeeJobVacancy = employeeJobVacancyRepository.findByEmployeeAndJobVacancy(employee, jobVacancy);

        if(employeeJobVacancy.isPresent()){
            jobVacancy.getRegisteredEmployees().remove(employeeJobVacancy);
            employee.getRegisteredJob().remove(employeeJobVacancy);
            employeeRepository.save(employee);
            jobVacancyRepository.save(jobVacancy);
            employeeJobVacancyRepository.delete(employeeJobVacancy.get());
        }
    }



    @Transactional
    public void subscribeToJobVacancy(UUID jobVacancyId, UUID employeeId) {
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

        if(isEmployeeRegisteredForJobVacancy(employeeId, jobVacancyId)){
                 throw new RuntimeException("Employee is already registered!");
        }

        EmployeeJobVacancy employeeJobVacancy = new EmployeeJobVacancy(employee, jobVacancy);
        employeeJobVacancyRepository.save(employeeJobVacancy);
    }


    public List<EmployeeJobVacancyDTO> getAllJobVacancyForEmployee(UUID employeeId) {

        List<EmployeeJobVacancy> jobVacancyResponseDTOList = employeeJobVacancyRepository.findByEmployee_Id(employeeId);
        return jobVacancyResponseDTOList
                .stream()
                .map(EmployeeJobVacancy :: getJobVacancy).map(EmployeeJobVacancyMapper:: listJobVacancyToEmployeeDTO).collect(Collectors.toList());
    }

    public boolean isEmployeeRegisteredForJobVacancy(UUID employeeId, UUID jobVacancyId) {
        return employeeJobVacancyRepository.existsByEmployeeIdAndJobVacancyId(employeeId, jobVacancyId);
    }
    

}
