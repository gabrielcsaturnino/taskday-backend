package com.example.taskday.mappers;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeCreateRequestDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.domain.exceptions.OperationException;

public class EmployeeMapper {
    public static Employee registerDTOToEmployee(EmployeeCreateRequestDTO employeeCreateRequestDTO) throws OperationException {
        Employee employee = new Employee();
        employee.setFirstName(employeeCreateRequestDTO.firstName());
        employee.setLastName(employeeCreateRequestDTO.lastName());
        employee.setEmail(employeeCreateRequestDTO.email());
        employee.setPhoneNumber(employeeCreateRequestDTO.phoneNumber());
        employee.setPassword(employeeCreateRequestDTO.password());
        employee.setCpf(employeeCreateRequestDTO.cpf());
        employee.setExperienceList(employeeCreateRequestDTO.experienceList());
        employee.setCity(employeeCreateRequestDTO.city());
        employee.setState(employeeCreateRequestDTO.state());
        employee.setPostalCode(employeeCreateRequestDTO.postalCode());
        employee.setAddressStreet(employeeCreateRequestDTO.addressStreet());
        employee.setAddress(employeeCreateRequestDTO.address());
        employee.setAddressComplement(employeeCreateRequestDTO.addressComplement());
        employee.setAddressNumber(employeeCreateRequestDTO.addressNumber());
        employee.setDateOfBirth(employeeCreateRequestDTO.dateOfBirth());
        return employee;
    }


    public static EmployeeResponseDTO employeeToEmployeeResponseDTO(Employee employee){
        EmployeeResponseDTO employeeResponseDTO = EmployeeResponseDTO.fullEmployee(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
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

        return employeeResponseDTO;
    }


    public static EmployeeResponseDTO partialEmployee(Employee employee){
        EmployeeResponseDTO employeeResponseDTO = EmployeeResponseDTO.partialEmployee(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber(),
                employee.getExperienceList(),
                employee.getCity(),
                employee.getState(),
                employee.getAddress(),
                employee.getDateOfBirth()
        );

        return employeeResponseDTO;
    }




}
