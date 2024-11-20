package com.example.APT.utils.jwt;

public enum ErrorCode {
    MEMBER_NOT_FOUND("Member not found"),
    DUPLICATE_USER_ID("이미 존재하는 사용자 ID입니다."),
    TOKEN_NOT_FOUND("토큰을 찾을 수 없습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다.");
    // 다른 에러 코드 추가 가능

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    }
