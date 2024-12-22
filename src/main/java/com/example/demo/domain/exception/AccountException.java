package com.example.demo.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 예외 처리 클래스
 * 계좌 관련 비즈니스 예외 클래스
 */
@Getter
@RequiredArgsConstructor
public class AccountException extends RuntimeException {
    /**
     * 에러 코드
     */
    private final ErrorCode errorCode;

    /**
     * 에러 메시지
     */
    private final String errorMessage;

    /**
     * ErrorCode만으로 예외를 생성하는 생성자
     * @param errorCode 에러 코드
     */
    public AccountException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
