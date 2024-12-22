package com.example.demo.domain.dto.response;

import com.example.demo.domain.transaction.Transaction;
import com.example.demo.domain.transaction.TransactionResultType;
import com.example.demo.domain.transaction.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 거래 정보 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    /**
     * 거래가 발생한 계좌번호
     */
    private String accountNumber;

    /**
     * 거래 유형 (USE: 사용, CANCEL: 취소)
     */
    private TransactionType transactionType;

    /**
     * 거래 결과 상태
     */
    private TransactionResultType transactionResult;

    /**
     * 거래 고유 식별자
     */
    private String transactionId;

    /**
     * 거래 금액
     */
    private Long amount;

    /**
     * 거래 일시
     */
    private LocalDateTime transactedAt;

    /**
     * Transaction 엔티티를 TransactionResponse DTO로 변환
     * @param transaction 거래 엔티티
     * @return TransactionResponse 객체
     */
    public static TransactionResponse from(Transaction transaction) {
        return TransactionResponse.builder() // TransactionResponse 객체 생성
            .accountNumber(transaction.getAccountNumber())
            .transactionType(transaction.getTransactionType())
            .transactionResult(transaction.getTransactionResultType())
            .transactionId(transaction.getTransactionId())
            .amount(transaction.getAmount())
            .transactedAt(transaction.getTransactedAt())
            .build();
    }
}
