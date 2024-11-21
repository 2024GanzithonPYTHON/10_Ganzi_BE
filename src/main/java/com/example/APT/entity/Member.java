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

import java.util.*;

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

    private String name;
    private int age;
    private String childName;


    @OneToMany(mappedBy = "user")
    private Set<UserCategory> userCategories;

    @OneToMany(mappedBy = "member")
    private Set<ActivityLog> activityLogs;

    @OneToMany(mappedBy = "member")
    private Set<Wish> likes;

    @OneToMany(mappedBy = "member")
    private Set<Activity> activities;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    public static Member newInstance(String loginId, String password, String address, int age, String name, String childName) {

        Member user = new Member();
        user.loginId = loginId;
        user.password = password;
        user.address = address;
        user.age = age;
        user.name = name;

        user.childName = childName;
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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

    // 1. 특정 필드 반환 메서드
    public Map<String, String> getDetails() {
        Map<String, String> details = new HashMap<>();
        details.put("name", this.name);
        details.put("childName", this.childName);
        details.put("age", String.valueOf(this.age));
        details.put("loginId", this.loginId);
        return details;
    }

    // 2. 특정 필드 수정 메서드
    public void updateDetails(String name, String childName, String age, String loginId) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
        if (childName != null && !childName.isEmpty()) {
            this.childName = childName;
        }
        if (age != null && !age.isEmpty()) {
            this.age = Integer.parseInt(age);
        }
        if (loginId != null && !loginId.isEmpty()) {
            this.loginId = loginId;
        }
    }
}



