package com.example.demo.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 예외 처리 클래스
 * 비즈니스 에러 코드 열거형
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * 사용자를 찾을 수 없는 경우
     */
    USER_NOT_FOUND("사용자가 없습니다."),

    /**
     * 계좌를 찾을 수 없는 경우
     */
    ACCOUNT_NOT_FOUND("계좌가 없습니다."),

    /**
     * 계좌가 거래 중인 경우
     */
    ACCOUNT_TRANSACTION_LOCK("해당 계좌는 현재 거래가 진행 중입니다."),

    /**
     * 거래를 찾을 수 없는 경우
     */
    TRANSACTION_NOT_FOUND("거래가 없습니다."),

    /**
     * 잔액이 부족한 경우
     */
    AMOUNT_EXCEED_BALANCE("거래 금액이 계좌 잔액보다 큽니다."),

    /**
     * 거래 계좌 불일치
     */
    TRANSACTION_ACCOUNT_UN_MATCH("이 거래는 해당 계좌에서 발생한 거래가 아닙니다."),

    /**
     * 거래 금액 불일치
     */
    CANCEL_MUST_FULLY("부분 취소는 허용되지 않습니다."),

    /**
     * 오래된 거래 취소 시도
     */
    TOO_OLD_ORDER_TO_CANCEL("1년이 지난 거래는 취소가 불가능합니다."),

    /**
     * 이미 해지된 계좌
     */
    ACCOUNT_ALREADY_UNREGISTERED("계좌가 이미 해지되었습니다."),

    /**
     * 잔액이 있는 계좌 해지 시도
     */
    BALANCE_NOT_EMPTY("잔액이 있는 계좌는 해지할 수 없습니다."),

    /**
     * 계좌 수 초과
     */
    MAX_ACCOUNT_PER_USER_10("사용자 최대 계좌는 10개입니다."),

    /**
     * 잔액 부족
     */
    INSUFFICIENT_BALANCE("잔액이 부족합니다.");

    /**
     * 에러 설명
     */
    private final String description;
}
