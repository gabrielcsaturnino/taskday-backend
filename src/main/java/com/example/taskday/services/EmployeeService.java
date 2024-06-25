package com.example.taskday.services;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRegisterDTO;
import com.example.taskday.domain.employee.EmployeeRequestDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;




    public void createEmployee(EmployeeRegisterDTO employeeRegisterDTO, String encryptedPassword) {
        Employee employee = this.registerDTOToEmployee(employeeRegisterDTO);
        employee.setPassword(encryptedPassword);
        employeeRepository.save(employee);
    }



    public List<EmployeeResponseDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToEmployeeResponseDTO)
                .collect(Collectors.toList());
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
                employee.getPostalCode(),
                employee.getAddressStreet(),
                employee.getAddressComplement(),
                employee.getAddressNumber(),
                employee.getAddress(),
                employee.getDateOfBirth(),
                employee.getRegisteredJob()
        );
    }




    public Employee registerDTOToEmployee(EmployeeRegisterDTO employeeRegisterDTO) {
        return new Employee(
                employeeRegisterDTO.firstName(),
                employeeRegisterDTO.lastName(),
                employeeRegisterDTO.email(),
                employeeRegisterDTO.phoneNumber(),
                employeeRegisterDTO.password(),
                employeeRegisterDTO.cpf(),
                employeeRegisterDTO.experienceList(),
                employeeRegisterDTO.city(),
                employeeRegisterDTO.state(),
                employeeRegisterDTO.postalCode(),
                employeeRegisterDTO.addressStreet(),
                employeeRegisterDTO.addressComplement(),
                employeeRegisterDTO.addressNumber(),
                employeeRegisterDTO.address(),
                employeeRegisterDTO.dateOfBirth());
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
                employeeRequestDTO.postalCode(),
                employeeRequestDTO.addressStreet(),
                employeeRequestDTO.addressComplement(),
                employeeRequestDTO.addressNumber(),
                employeeRequestDTO.address(),
                employeeRequestDTO.dateOfBirth());
    }




}
