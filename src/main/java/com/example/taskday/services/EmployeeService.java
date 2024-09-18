package com.example.taskday.services;

import br.com.caelum.stella.validation.CPFValidator;
import com.example.taskday.domain.employee.*;

import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.mappers.EmployeeMapper;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanyRepository companyRepository;

    public void createEmployee(EmployeeRegisterDTO employeeRegisterDTO, String encryptedPassword) throws OperationException {

        if(employeeRepository.existsByEmail(employeeRegisterDTO.email()) || companyRepository.existsByEmail(employeeRegisterDTO.email())){
            throw new OperationException("Email ja cadastrado!");
        }

        Employee employee = EmployeeMapper.registerDTOToEmployee(employeeRegisterDTO);
        employee.setPassword(encryptedPassword);
        employeeRepository.save(employee);
    }

    public void changeAccount(EmployeeChangeAccountDTO employeeChangeAccountDTO, Employee employee) throws OperationException {
        if(employeeRepository.existsByEmail(employeeChangeAccountDTO.email()) || companyRepository.existsByEmail(employeeChangeAccountDTO.email())){
            throw new OperationException("Email ja cadastrado!");
        }
        employee.setFirstName(employeeChangeAccountDTO.firstName());
        employee.setLastName(employeeChangeAccountDTO.lastName());
        employee.setEmail(employeeChangeAccountDTO.email());
        employee.setPhoneNumber(employeeChangeAccountDTO.phoneNumber());
        employee.setCity(employeeChangeAccountDTO.city());
        employee.setState(employeeChangeAccountDTO.state());
        employee.setExperienceList(employeeChangeAccountDTO.experienceList());
        employee.setAddress(employeeChangeAccountDTO.address());
        employee.setAddressComplement(employeeChangeAccountDTO.addressComplement());
        employee.setAddressNumber(employeeChangeAccountDTO.addressNumber());
        employee.setAddressStreet(employeeChangeAccountDTO.addressStreet());
        employeeRepository.save(employee);
    }

    public void changePassword(String encryptedPassword, Employee employee){
        employee.setPassword(encryptedPassword);
        employeeRepository.save(employee);
    }

    @Transactional
    public EmployeeResponseDTO findEmployeeById(UUID employeeId) {
        Optional<Employee> employee = this.employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            throw new RuntimeException("Employee not found!");
        }
        return EmployeeMapper.employeeToEmployeeResponseDTO(employee.get());
    }


}
