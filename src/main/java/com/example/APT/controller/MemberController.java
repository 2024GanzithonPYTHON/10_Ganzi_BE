package com.example.APT.controller;

import com.example.APT.entity.Member;
import com.example.APT.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 사용자 정보 조회
    @GetMapping("/member/details")
    public ResponseEntity<Map<String, String>> getMemberDetails() {
        Member member = memberService.getCurrentMember();
        return ResponseEntity.ok(member.getDetails());
    }

    // 사용자 정보 수정
    @PostMapping("/member/update")
    public ResponseEntity<String> updateMemberDetails(@RequestBody Map<String, String> updates) {
        Member member = memberService.getCurrentMember();
        member.updateDetails(
                updates.get("name"),
                updates.get("childName"),
                updates.get("age"),
                updates.get("loginId")
        );

        memberService.save(member);
        return ResponseEntity.ok("Member details updated successfully");
    }

}
