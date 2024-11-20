package com.example.APT.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String address;

    private int age;
    private String name;

    @OneToMany(mappedBy = "user")
    private Set<UserCategory> userCategories;

    @OneToMany(mappedBy = "user")
    private Set<Activity> activities;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    // Static factory method
    public static Member newInstance(String loginId, String password, String address, int age, String name) {
        Member user = new Member();
        user.loginId = loginId;
        user.password = password;
        user.address = address;
        user.age = age;
        user.name = name;
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Filter out null or empty roles
        return this.roles.stream()
                .filter(role -> role != null && !role.isEmpty()) // Filter out invalid roles
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

