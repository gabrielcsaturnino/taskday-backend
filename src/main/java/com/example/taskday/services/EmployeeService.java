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

import java.time.LocalDate;
import java.util.Map;
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
        employee.setCreatedBy(LocalDate.now());
        employee.setUpdatedBy(LocalDate.now());
        employeeRepository.save(employee);
    }

    public void changeAccount(EmployeeChangeAccountDTO employeeChangeAccountDTO, Employee employee) throws OperationException {

        if(employeeChangeAccountDTO.email().isPresent()){
            if(employeeRepository.existsByEmail(employeeChangeAccountDTO.email().get()) || companyRepository.existsByEmail(employeeChangeAccountDTO.email().get())){
                throw new OperationException("Email ja cadastrado!");
            }
        }

        employeeChangeAccountDTO.firstName().ifPresent(employee :: setFirstName);
        employeeChangeAccountDTO.lastName().ifPresent(employee :: setLastName);
        employeeChangeAccountDTO.email().ifPresent(employee :: setEmail);
        employeeChangeAccountDTO.phoneNumber().ifPresent(employee :: setPhoneNumber);
        employeeChangeAccountDTO.city().ifPresent(employee :: setCity);
        employeeChangeAccountDTO.experienceList().ifPresent(employee :: setExperienceList);
        employeeChangeAccountDTO.address().ifPresent(employee :: setAddress);
        employeeChangeAccountDTO.addressComplement().ifPresent(employee :: setAddressComplement);
        employeeChangeAccountDTO.addressNumber().ifPresent(employee :: setAddressNumber);
        employeeChangeAccountDTO.addressStreet().ifPresent(employee :: setAddressStreet);
        employee.setUpdatedBy(LocalDate.now());
        employeeRepository.save(employee);

    }



    public void changePassword(String encryptedPassword, Employee employee){
        employee.setPassword(encryptedPassword);
        employee.setUpdatedBy(LocalDate.now());
        employeeRepository.save(employee);
    }

    @Transactional
    public EmployeeResponseDTO findEmployeeById(UUID employeeId) throws OperationException {
        Optional<Employee> employee = this.employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            throw new OperationException("Employee not found!");
        }
        return EmployeeMapper.employeeToEmployeeResponseDTO(employee.get());
    }



}
