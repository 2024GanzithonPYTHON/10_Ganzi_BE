package com.example.APT.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
