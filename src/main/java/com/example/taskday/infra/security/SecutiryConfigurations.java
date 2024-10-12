package com.example.taskday.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecutiryConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       return httpSecurity
               .csrf(csrf -> csrf.disable())
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(authorize -> authorize
                       .requestMatchers(HttpMethod.POST, "/auth/login/employee").permitAll()
                       .requestMatchers(HttpMethod.POST, "/job-vacancies").hasRole("COMPANY")
                       .requestMatchers(HttpMethod.POST, "/auth/register/employee").permitAll()
                       .requestMatchers(HttpMethod.POST, "/auth/register/company").permitAll()
                       .requestMatchers(HttpMethod.POST, "/auth/resendcode").permitAll()
                       .requestMatchers(HttpMethod.POST, "/auth/login/company").permitAll()
                       .requestMatchers(HttpMethod.POST, "/auth/confirmcode").hasRole("INACTIVE")
                       .requestMatchers(HttpMethod.GET, "/job-vacancies").permitAll()
                       .requestMatchers(HttpMethod.GET, "/job-vacancies/{jobVacancyId}/employees").hasRole("COMPANY")
                       .requestMatchers(HttpMethod.GET, "/employees").permitAll()
                       .requestMatchers(HttpMethod.GET, "/employees/jobs/subscriptions").hasRole("EMPLOYEE")
                       .requestMatchers(HttpMethod.POST, "/employees/jobs/subscribe").hasRole("EMPLOYEE")
                       .requestMatchers(HttpMethod.GET, "/employees/me").hasRole("EMPLOYEE")
                       .requestMatchers(HttpMethod.PUT, "employees/account").hasRole("EMPLOYEE")
                       .requestMatchers(HttpMethod.PUT, "/employees/password").hasRole("EMPLOYEE")
                       .requestMatchers(HttpMethod.POST, "/employees/request-password-change").hasRole("EMPLOYEE")
                       .requestMatchers(HttpMethod.GET, "/employees/email").permitAll()
                       .requestMatchers(HttpMethod.DELETE, "/employees/jobs/{jobVacancyId}/unsubscribe").hasRole("EMPLOYEE")
                       .requestMatchers(HttpMethod.GET, "/job-vacancies/{jobVacancyId}/employees").hasRole("COMPANY")
                       .requestMatchers(HttpMethod.DELETE, "/job-vacancies/").hasRole("COMPANY")
                       .requestMatchers(HttpMethod.GET, "/companies").permitAll()
                       .requestMatchers(HttpMethod.GET, "/auth/isEmployee").permitAll()
                       .requestMatchers(HttpMethod.GET, "/auth/isCompany").permitAll()
                       .requestMatchers(HttpMethod.PUT, "/job-vacancies/{jobVacancyId}").hasRole("COMPANY")
                       .requestMatchers(HttpMethod.GET, "/actuator/prometheus").permitAll()



                       .anyRequest().authenticated()
               )
               .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
    }




}
