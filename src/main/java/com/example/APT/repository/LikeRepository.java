package com.example.APT.repository;


import com.example.APT.entity.Activity;
import com.example.APT.entity.Wish;
import com.example.APT.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Wish, Long> {
//    Optional<Wish> findByMemberAndActivity(Member member, Activity activity);

    Optional<Wish> findByMemberIdAndActivityId(Long memberId, Long activityId);
}
