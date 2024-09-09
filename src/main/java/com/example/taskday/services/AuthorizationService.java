package com.example.taskday.services;


import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails user = employeeRepository.findByEmail(username);
        if (user == null) {
            user = companyRepository.findByEmail(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return user;
    }

}
