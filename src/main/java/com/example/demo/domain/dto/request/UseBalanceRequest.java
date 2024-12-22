package com.example.demo.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * dto -> 요청 및 응답 객체
 * 잔액 사용 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UseBalanceRequest {
    /**
     * 잔액 사용을 요청하는 사용자 ID
     * 필수값
     */
    @NotNull
    private Long userId;

    /**
     * 잔액을 사용할 계좌번호
     * 필수값이며 공백 불가
     */
    @NotBlank
    private String accountNumber;

    /**
     * 사용할 금액
     * 필수값이며 최소 10원 이상
     */
    @NotNull
    @Min(10)
    private Long amount;

    /**
     * 테스트용 생성자
     */
    public UseBalanceRequest(String number, long l) {
    }
}
