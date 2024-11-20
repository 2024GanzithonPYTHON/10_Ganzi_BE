package com.example.APT.utils.jwt;



public class CustomException extends RuntimeException { // RuntimeException을 상속
    private final ErrorCode errorCode; // ErrorCode 필드 추가

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // ErrorCode의 메시지를 부모 클래스에 전달
        this.errorCode = errorCode; // errorCode 필드 초기화
    }

    public ErrorCode getErrorCode() {
        return errorCode; // ErrorCode를 반환하는 메서드 추가
    }
}
