package com.example.taskday.services;

import com.example.taskday.domain.employee.*;

import com.example.taskday.mappers.EmployeeMapper;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;


    @Autowired
    JobVacancyRepository jobVacancyRepository;




    public void createEmployee(EmployeeRegisterDTO employeeRegisterDTO, String encryptedPassword) {
        Employee employee = EmployeeMapper.registerDTOToEmployee(employeeRegisterDTO);
        employee.setPassword(encryptedPassword);
        employeeRepository.save(employee);
    }

    public EmployeeResponseDTO findEmployeeById(UUID employeeId) {
        Optional<Employee> employee = this.employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            throw new RuntimeException("Employee not found!");
        }
        return EmployeeMapper.employeeToEmployeeResponseDTO(employee.get());
    }






}
