package com.example.APT.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access= AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignupResponseDto {

    private Long userId;
    private String loginId;

    public static MemberSignupResponseDto of(Long userId, String loginId) {
        return new MemberSignupResponseDto(userId, loginId);
    }
}
