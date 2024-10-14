package com.example.taskday.controllers;

import com.example.taskday.domain.company.Company;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping()
    public String home() {
        return "index"; // Retorna o template home.html
    }

    @GetMapping("/employee/dashboard")
    public String dashEmployee(){
        return "dashboardemployee";
    }

    @GetMapping("dashboardcompany")
    public void dashCompany(){

    }
}