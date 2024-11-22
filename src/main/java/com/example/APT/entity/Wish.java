package com.example.APT.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@IdClass(WishId.class)
public class Wish {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Id
    @Column(name = "activity_id")
    private Long activityId;

    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "activity_id", nullable = false)
//    private Activity activity;
}
