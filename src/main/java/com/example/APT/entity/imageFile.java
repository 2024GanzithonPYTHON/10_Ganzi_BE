package com.example.APT.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class imageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "mediumblob")
    private byte[] image;
    private String imageName;
    private LocalDateTime uploadTime;

    @PrePersist
    protected void onCreate() {
        uploadTime = LocalDateTime.now();
    }
}
