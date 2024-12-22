package com.example.demo.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * dto -> 요청 및 응답 객체
 * 잔액 사용 취소 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CancelBalanceRequest {
    /**
     * 취소할 거래의 거래 ID
     * 필수값이며 공백 불가
     */
    @NotBlank
    private String transactionId;

    /**
     * 취소할 계좌번호
     * 필수값이며 공백 불가
     */
    @NotBlank
    private String accountNumber;

    /**
     * 취소할 금액
     * 필수값이며 최소 10원 이상
     */
    @NotNull
    @Min(10)
    private Long amount;
}
