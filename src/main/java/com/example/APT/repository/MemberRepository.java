package com.example.APT.repository;

import com.example.APT.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

    Optional<Member> findById(Long id);
    boolean existsById(Long id);
//    Optional<Member> findByLoginId(String username);
}
