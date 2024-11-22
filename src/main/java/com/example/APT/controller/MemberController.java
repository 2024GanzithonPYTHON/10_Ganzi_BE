package com.example.APT.controller;

import com.example.APT.dto.CategoryRequestDTO;
import com.example.APT.entity.Member;
import com.example.APT.service.MemberService;
import com.example.APT.service.UserCategoryService;
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
    private final UserCategoryService userCategoryService;

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
    // 사용자 선호 카테고리 추가
    @PostMapping("/member/category")
    public ResponseEntity<String> addUserCategories(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        Member currentUser = memberService.getCurrentMember(); // 현재 로그인한 사용자 정보 가져오기

        // 서비스 호출 - 여러 카테고리를 추가
        userCategoryService.addUserCategories(categoryRequestDTO, currentUser);

        return ResponseEntity.ok("카테고리가 사용자에게 성공적으로 추가되었습니다.");
    }

}
