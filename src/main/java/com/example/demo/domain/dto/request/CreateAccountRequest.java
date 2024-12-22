package com.example.demo.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * dto -> 요청 및 응답 객체
 * 계좌 생성 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    /**
     * 계좌를 생성할 사용자 ID
     * 필수값
     */
    @NotNull
    private Long userId;

    /**
     * 계좌 생성 시 초기 입금액
     * 필수값이며 0원 이상
     */
    @NotNull
    @Min(0)
    private Long initialBalance;
}
