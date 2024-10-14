package com.example.taskday.services;

import com.example.taskday.domain.employee.*;

import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.enums.RoleType;
import com.example.taskday.mappers.EmployeeMapper;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmailService emailService;

    public void createEmployee(EmployeeCreateRequestDTO employeeCreateRequestDTO, String encryptedPassword) throws OperationException {

        if (employeeRepository.existsByEmail(employeeCreateRequestDTO.email())
                || companyRepository.existsByEmail(employeeCreateRequestDTO.email())
                || employeeRepository.existsByCpf(employeeCreateRequestDTO.cpf())
                || companyRepository.existsByOwnerCpf(employeeCreateRequestDTO.cpf())) {
            throw new OperationException("Erro ao cadastrar! Verifique os dados inseridos.");
        }

        Employee employee = EmployeeMapper.registerDTOToEmployee(employeeCreateRequestDTO);
        employee.setPassword(encryptedPassword);
        employee.setCreatedBy(LocalDate.now());
        employee.setUpdatedBy(LocalDate.now());

        employee.setConfirmationCode(generateConfirmationCode());
        emailService.sendEmail(employee.getEmail(), employee.getConfirmationCode());
        employee.setRoleType(RoleType.INACTIVE);
        employee.setEnabled(true);
        employeeRepository.save(employee);
        if(!employeeRepository.existsByEmail(employee.getEmail())){
            throw new OperationException("Erro!");
        }
    }

    public void confirmationAccount(Employee employee, String code) throws OperationException {

        if(employee.getRoleType() == RoleType.EMPLOYEE){
            throw new OperationException("Usuário ja foi autenticado!");
        }

        if(employee.getConfirmationCode().equals(code)){
            employee.setRoleType(RoleType.EMPLOYEE);
            employee.setConfirmationCode(null);
            employee.setEnabled(true);
            employeeRepository.save(employee);
        }else {
            throw new OperationException("Código de confirmação inválido!");
        }
    }

    public void resendConfirmationCode(String email) throws OperationException {
        if(employeeRepository.existsByEmail(email)){
            Employee employee = (Employee) employeeRepository.findByEmail(email);
                employee.setConfirmationCode(generateConfirmationCode());
                employeeRepository.save(employee);
                emailService.sendEmail(email, employee.getConfirmationCode());
        }else {
            throw new OperationException("Email não cadastrado!");
        }
    }


    private String generateConfirmationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Gera um código de 6 dígitos
        return String.valueOf(code);
    }

    public void changeAccount(EmployeeChangeAccountRequestDTO employeeChangeAccountRequestDTO, Employee employee) throws OperationException {

        if(employeeChangeAccountRequestDTO.email().isPresent()){
            if(employeeRepository.existsByEmail(employeeChangeAccountRequestDTO.email().get()) || companyRepository.existsByEmail(employeeChangeAccountRequestDTO.email().get())){
                throw new OperationException("Email ja cadastrado!");
            }
        }

        employeeChangeAccountRequestDTO.firstName().ifPresent(employee :: setFirstName);
        employeeChangeAccountRequestDTO.lastName().ifPresent(employee :: setLastName);
        employeeChangeAccountRequestDTO.email().ifPresent(employee :: setEmail);
        employeeChangeAccountRequestDTO.phoneNumber().ifPresent(employee :: setPhoneNumber);
        employeeChangeAccountRequestDTO.city().ifPresent(employee :: setCity);
        employeeChangeAccountRequestDTO.experienceList().ifPresent(employee :: setExperienceList);
        employeeChangeAccountRequestDTO.address().ifPresent(employee :: setAddress);
        employeeChangeAccountRequestDTO.addressComplement().ifPresent(employee :: setAddressComplement);
        employeeChangeAccountRequestDTO.addressNumber().ifPresent(employee :: setAddressNumber);
        employeeChangeAccountRequestDTO.addressStreet().ifPresent(employee :: setAddressStreet);
        employee.setUpdatedBy(LocalDate.now());
        employeeRepository.save(employee);

    }



    public void changePassword(String encryptedPassword, Employee employee, String code) throws OperationException {
        if(!employee.getConfirmationCode().equals(code)){
            throw new OperationException("Código inválido!");
        }

        employee.setConfirmationCode(null);
        employee.setPassword(encryptedPassword);
        employeeRepository.save(employee);
    }


    @Transactional
    public EmployeeResponseDTO findEmployee(Employee employee) throws OperationException {
        Optional<Employee> employee1 = this.employeeRepository.findById(employee.getId());
        if (!employee1.isPresent()) {
            throw new OperationException("Funcionario não foi encontrado!");
        }
        return EmployeeMapper.employeeToEmployeeResponseDTO(employee1.get());
    }


    @Transactional
    public EmployeeResponseDTO findPartialEmployee(UUID employeeId) throws OperationException {
        Optional<Employee> employee1 = this.employeeRepository.findById(employeeId);
        if (!employee1.isPresent()) {
            throw new OperationException("Funcionario não foi encontrado!");
        }
        return EmployeeMapper.partialEmployee(employee1.get());
    }


    @Transactional
    public void deleteAccount(Employee employee){
        employeeRepository.delete(employee);
    }


}
