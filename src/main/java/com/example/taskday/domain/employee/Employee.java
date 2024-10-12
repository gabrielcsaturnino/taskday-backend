package com.example.taskday.domain.employee;

import br.com.caelum.stella.validation.CPFValidator;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.validations.MinAge;
import com.example.taskday.enums.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity @Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Table(name = "employee")
public class Employee implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String firstName;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String lastName;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    @Email(message = "Email invalido!")
    private String email;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String phoneNumber;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String password;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String cpf;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> experienceList = new ArrayList<>();
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String city;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String state;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String postalCode;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String addressStreet;
    private String addressComplement;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String addressNumber;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String address;

    @NotNull(message = "Esse campo não pode ser nulo")
    @PastOrPresent(message = "Selecione uma data valida!")
    @MinAge(value = 18, message = "O usuário deve ter pelo menos 18 anos")
    private LocalDate dateOfBirth;

    private RoleType roleType;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeJobVacancy> registeredJob = new ArrayList<>();


    private boolean enabled = false;

    private String confirmationCode;

    @CreatedDate
    private LocalDate createdBy;

    @LastModifiedDate
    private LocalDate updatedBy;

    public Employee(String firstName, String lastName, String email, String phoneNumber, String password, List<String> experienceList, String city, String state, String postalCode, String addressStreet, String addressComplement, String addressNumber, String address, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.experienceList = experienceList;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.addressStreet = addressStreet;
        this.addressComplement = addressComplement;
        this.addressNumber = addressNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;


    }

    public void setCpf(String cpf) throws OperationException {
        try {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.assertValid(cpf);
            this.cpf = cpf;
        }catch (Exception e){
            throw new OperationException("CPF invalido");
        }
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.roleType == RoleType.INACTIVE) {
            return List.of(new SimpleGrantedAuthority("ROLE_INACTIVE"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        }
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
