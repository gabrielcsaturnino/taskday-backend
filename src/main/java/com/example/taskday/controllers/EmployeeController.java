package com.example.taskday.controllers;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRequestDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<Void>  addEmployee(@RequestBody EmployeeRequestDTO employeeDTO) {
       employeeService.createEmployee(employeeDTO);
       return new ResponseEntity(HttpStatus.CREATED);
    }


    @GetMapping("/see")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


}
