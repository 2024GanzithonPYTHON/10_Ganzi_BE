package com.example.APT.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
public class ActivityLog extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String content;

    @Column(length = 30)
    private String oneLine;

//    imageFile 필드 추가
    private String imageUrl;
    private LocalDateTime uploadTime;

    private String place;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;





}
