package com.example.demo.domain.dto.response;

import com.example.demo.domain.transaction.Transaction;
import com.example.demo.domain.transaction.TransactionResultType;
import com.example.demo.domain.transaction.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private String accountNumber;
    private TransactionType transactionType;
    private TransactionResultType transactionResult;
    private String transactionId;
    private Long amount;
    private LocalDateTime transactedAt;

    public static TransactionResponse from(Transaction transaction) {
        return TransactionResponse.builder()
            .accountNumber(transaction.getAccountNumber())
            .transactionType(transaction.getTransactionType())
            .transactionResult(transaction.getTransactionResultType())
            .transactionId(transaction.getTransactionId())
            .amount(transaction.getAmount())
            .transactedAt(transaction.getTransactedAt())
            .build();
    }
}
