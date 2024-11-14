package com.example.APT.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    private Set<UserCategory> userCategories;
}
