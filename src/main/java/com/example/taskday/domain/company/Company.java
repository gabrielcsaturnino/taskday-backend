package com.example.taskday.domain.company;


import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

    private String name;
    private String cnpj;
    private String addressStreet;
    private String addressComplement;
    private String addressNumber;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String password;
    private String email;
    private String phoneNumber;
    private String ownerName;

    @OneToMany(mappedBy = "company" , cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<JobVacancy> jobList = new ArrayList<>();

    private RoleType roleType;

    public Company(String name, String cnpj, String addressStreet, String addressComplement, String addressNumber, String address, String city, String state, String postalCode, String password, String email, String phoneNumber, String ownerName) {
        this.name = name;
        this.cnpj = cnpj;
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
