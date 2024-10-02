package com.example.taskday.domain.company;


import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.enums.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity @Table(name = "company") @Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Company implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String name;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String cnpj;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String addressStreet;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String addressComplement;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String addressNumber;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String address;
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
    private String password;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    @Email(message = "Email invalido!")
    private String email;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String phoneNumber;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String ownerName;
    @NotBlank(message = "Esse campo não pode estar em branco")
    @NotNull(message = "Esse campo não pode ser nulo")
    private String ownerCpf;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    private List<JobVacancy> jobList = new ArrayList<>();

    private RoleType roleType;

    @CreatedDate
    private LocalDate createdBy;

    @LastModifiedDate
    private LocalDate updatedBy;

    public Company(String name, String addressStreet, String addressComplement, String addressNumber, String address, String city, String state, String postalCode, String password, String email, String phoneNumber,String ownerName) {
        this.name = name;
        this.addressStreet = addressStreet;
        this.addressComplement = addressComplement;
        this.addressNumber = addressNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ownerName = ownerName;
        this.roleType =  RoleType.COMPANY;

    }

    public void setCnpj(String cnpj) throws OperationException {
        try {
            CNPJValidator cnpjValidator = new CNPJValidator();
            cnpjValidator.assertValid(cnpj);
            this.cnpj = cnpj;
        }catch (Exception e){
            throw  new OperationException("CNPJ invalido");
        }
    }

    public void setOwnerCpf(String ownerCpf) throws OperationException {
        try{
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.assertValid(ownerCpf);
            this.ownerCpf = ownerCpf;
        }catch (Exception e){
            throw new OperationException("CPF invalido");
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_COMPANY"));
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
        return UserDetails.super.isEnabled();
    }
}
