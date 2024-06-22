package com.example.taskday.services;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRequestDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;



    public void createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        employeeRepository.save(this.convertToEmployee(employeeRequestDTO));
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToEmployeeResponseDTO)
                .collect(Collectors.toList());
    }


    public EmployeeRequestDTO convertToEmployeeRequestDTO(Employee employee) {
        return new EmployeeRequestDTO(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getPassword(),
                employee.getCpf(),
                employee.getExperienceList(),
                employee.getCity(),
                employee.getState(),
                employee.getCep(),
                employee.getAddressStreet(),
                employee.getAddressComplement(),
                employee.getAddressNumber(),
                employee.getAddress(),
                employee.getDateOfBirth(),
                employee.getRegisteredJob()
        );
    }

    public EmployeeResponseDTO convertToEmployeeResponseDTO(Employee employee) {
        return new EmployeeResponseDTO(employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getPassword(),
                employee.getCpf(),
                employee.getExperienceList().stream().map(Object :: toString ).collect(Collectors.toList()),
                employee.getCity(),
                employee.getState(),
                employee.getCep(),
                employee.getAddressStreet(),
                employee.getAddressComplement(),
                employee.getAddressNumber(),
                employee.getAddress(),
                employee.getDateOfBirth(),
                employee.getRegisteredJob()
        );
    }

    public Employee convertToEmployee(EmployeeRequestDTO employeeRequestDTO) {
        return new Employee(
                employeeRequestDTO.firstName(),
                employeeRequestDTO.lastName(),
                employeeRequestDTO.email(),
                employeeRequestDTO.phoneNumber(),
                employeeRequestDTO.password(),
                employeeRequestDTO.cpf(),
                employeeRequestDTO.experienceList(),
                employeeRequestDTO.city(),
                employeeRequestDTO.state(),
                employeeRequestDTO.cep(),
                employeeRequestDTO.addressStreet(),
                employeeRequestDTO.addressComplement(),
                employeeRequestDTO.addressNumber(),
                employeeRequestDTO.address(),
                employeeRequestDTO.dateOfBirth(),
                employeeRequestDTO.registeredJob());
    }




}
