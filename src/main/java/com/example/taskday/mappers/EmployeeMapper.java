package com.example.taskday.mappers;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRegisterDTO;
import com.example.taskday.domain.employee.EmployeeRegisteredDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;

import java.util.stream.Collectors;

public class EmployeeMapper {
    public static Employee registerDTOToEmployee(EmployeeRegisterDTO employeeRegisterDTO){
        Employee employee = new Employee();
        employee.setFirstName(employeeRegisterDTO.firstName());
        employee.setLastName(employeeRegisterDTO.lastName());
        employee.setEmail(employeeRegisterDTO.email());
        employee.setPhoneNumber(employeeRegisterDTO.phoneNumber());
        employee.setPassword(employeeRegisterDTO.password());
        employee.setCpf(employeeRegisterDTO.cpf());
        employee.setExperienceList(employeeRegisterDTO.experienceList());
        employee.setCity(employeeRegisterDTO.city());
        employee.setState(employeeRegisterDTO.state());
        employee.setPostalCode(employeeRegisterDTO.postalCode());
        employee.setAddressStreet(employeeRegisterDTO.addressStreet());
        employee.setAddress(employeeRegisterDTO.address());
        employee.setAddressComplement(employeeRegisterDTO.addressComplement());
        employee.setAddressNumber(employeeRegisterDTO.addressNumber());
        employee.setDateOfBirth(employeeRegisterDTO.dateOfBirth());
        return employee;
    }

    public static EmployeeResponseDTO employeeToEmployeeResponseDTO(Employee employee){
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(
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
                employee.getDateOfBirth(),
                employee.getRegisteredJob()
                        .stream()
                        .map(employeeJobVacancy -> EmployeeJobVacancyMapper.listJobVacancyToEmployeeDTO(employeeJobVacancy.getJobVacancy())).collect(Collectors.toList()));

        return employeeResponseDTO;
    }

    public static EmployeeRegisteredDTO employeeToEmployeeRegisteredDTO(Employee employee, EmployeeJobVacancy employeeJobVacancy){
        EmployeeRegisteredDTO employeeRegisteredDTO = new EmployeeRegisteredDTO(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getExperienceList(),
                employee.getCity(),
                employee.getState(),
                employeeJobVacancy.getPont()
        );
        return employeeRegisteredDTO;
    }


}
