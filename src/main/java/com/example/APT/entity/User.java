package com.example.APT.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Getter
public class User {

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
}
