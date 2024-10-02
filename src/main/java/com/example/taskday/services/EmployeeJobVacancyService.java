package com.example.taskday.services;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRegisteredDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancyDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.enums.Status;
import com.example.taskday.mappers.EmployeeJobVacancyMapper;
import com.example.taskday.mappers.EmployeeMapper;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;



@Service
public class EmployeeJobVacancyService {
    @Autowired
    EmployeeJobVacancyRepository employeeJobVacancyRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    JobVacancyRepository jobVacancyRepository;



    @Transactional
    public void unsubscribe(UUID jobVacancyId, UUID employeeId) throws OperationException {

        JobVacancy jobVacancy = jobVacancyRepository.findById(jobVacancyId)
                .orElseThrow(() -> new OperationException("Vaga não encontrada"));


        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new OperationException("Funcionário não encontrado"));

        Optional<EmployeeJobVacancy> employeeJobVacancyOpt = employeeJobVacancyRepository.findByEmployeeAndJobVacancy(employee, jobVacancy);

        if (employeeJobVacancyOpt.isPresent()) {
            employeeJobVacancyRepository.delete(employeeJobVacancyOpt.get());
        } else {
            throw new OperationException("Subscription not found");
        }
    }



    @Transactional
    public void subscribeToJobVacancy(UUID jobVacancyId, UUID employeeId) throws OperationException {

        JobVacancy jobVacancy = jobVacancyRepository.findById(jobVacancyId)
                .orElseThrow(() -> new OperationException("Vaga não encontrada"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new OperationException("Funcionário não encontrado"));

        if(isEmployeeRegisteredForJobVacancy(employeeId, jobVacancyId)){
                 throw new OperationException("Você ja esta registrado nessa vaga!");
        }

        if(jobVacancy.getStatus() == Status.INACTIVE){
            throw new OperationException("Vaga inativa!");
        }

        EmployeeJobVacancy employeeJobVacancy = new EmployeeJobVacancy(employee, jobVacancy);


        employeeJobVacancyRepository.save(employeeJobVacancy);
        if(!jobVacancy.getDesiredExperience().isEmpty() && !employee.getExperienceList().isEmpty()) {
            this.setPoint(jobVacancyId, employeeId);
        }
    }

    public double getPoints(UUID jobVacancyId, UUID employeeId) throws OperationException {
        JobVacancy jobVacancy = jobVacancyRepository.findById(jobVacancyId)
                .orElseThrow(() -> new OperationException("Vaga não encontrada"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new OperationException("Funcionário não encontrado"));

        EmployeeJobVacancy employeeJobVacancy = employeeJobVacancyRepository.findByEmployeeAndJobVacancy(employee, jobVacancy)
                .orElseThrow(() -> new OperationException("Erro interno"));

        return employeeJobVacancy.getPoint();
    }


    public List<EmployeeJobVacancyDTO> getAllJobVacancyForEmployee(UUID employeeId) {

        List<EmployeeJobVacancy> jobVacancyResponseDTOList = employeeJobVacancyRepository.findByEmployee_Id(employeeId);
        return jobVacancyResponseDTOList
                .stream()
                .map(EmployeeJobVacancy :: getJobVacancy).map(EmployeeJobVacancyMapper:: listJobVacancyToEmployeeDTO).collect(Collectors.toList());
    }

    public void setPoint(UUID jobVacancyId, UUID employeeId) throws OperationException {
        JobVacancy jobVacancy = jobVacancyRepository.findById(jobVacancyId)
                .orElseThrow(() -> new OperationException("Vaga não encontrada"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new OperationException("Funcionário não encontrado"));

        EmployeeJobVacancy employeeJobVacancy = employeeJobVacancyRepository.findByEmployeeAndJobVacancy(employee, jobVacancy)
                .orElseThrow(() -> new OperationException("Erro interno"));


        int points = 0;
        Set<String> desiredExperienceSet = new HashSet<>(jobVacancy.getDesiredExperience());

        if (!desiredExperienceSet.isEmpty()) {
            for (String experience : employee.getExperienceList()) {
                if (desiredExperienceSet.contains(experience)) {
                    points += 200;
                }
            }
        }

        employeeRepository.save(employee);
        jobVacancyRepository.save(jobVacancy);
        employeeJobVacancyRepository.save(employeeJobVacancy);

    }

    public List<EmployeeRegisteredDTO> getAllEmployeeRegisteredByJobVacancy(UUID jobVacancyId) {
        return employeeJobVacancyRepository.findByJobVacancy_Id(jobVacancyId)
                .stream()
                .map(employeeJobVacancy -> EmployeeMapper.employeeToEmployeeRegisteredDTO(employeeJobVacancy.getEmployee(), employeeJobVacancy))
                .collect(Collectors.toList());
    }


    public boolean isEmployeeRegisteredForJobVacancy(UUID employeeId, UUID jobVacancyId) {
        return employeeJobVacancyRepository.existsByEmployeeIdAndJobVacancyId(employeeId, jobVacancyId);
    }

}
