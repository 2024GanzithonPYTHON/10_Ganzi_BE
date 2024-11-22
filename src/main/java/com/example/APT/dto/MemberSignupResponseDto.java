package com.example.APT.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignupResponseDto {

    private Long userId;
    private String loginId;
    private List<String> categoryNames; // 선택한 카테고리

    public static MemberSignupResponseDto of(Long userId, String loginId, List<String> categoryNames) {
        return new MemberSignupResponseDto(userId, loginId, categoryNames);
    }
}
