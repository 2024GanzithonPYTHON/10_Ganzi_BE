package com.example.APT.utils.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // RefreshToken을 키로 조회하는 메서드
    Optional<RefreshToken> findByKey(String key);
}
