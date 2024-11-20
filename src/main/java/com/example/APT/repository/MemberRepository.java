package com.example.APT.repository;

import com.example.APT.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // loginId로 사용자 조회
    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

    // 특정 ID 존재 여부 확인
    boolean existsById(Long id);
}
