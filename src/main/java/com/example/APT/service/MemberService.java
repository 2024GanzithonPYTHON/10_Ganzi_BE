package com.example.APT.service;

import com.example.APT.entity.Member;
import com.example.APT.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 현재 인증된 사용자 정보를 가져오기
    public Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        String loginId = authentication.getName(); // JWT에서 추출한 사용자 로그인 ID
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + loginId));
    }

    // 사용자 저장 (수정 포함)
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    // 특정 사용자 조회 (ID 기준)
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    // 특정 사용자 조회 (loginId 기준)
    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + loginId));
    }

    // 모든 사용자 목록 가져오기
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // 사용자 삭제
    public void deleteById(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }
}

