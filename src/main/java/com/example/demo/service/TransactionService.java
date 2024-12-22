package com.example.demo.service;

import com.example.demo.domain.account.Account;
import com.example.demo.domain.account.AccountStatus;
import com.example.demo.domain.dto.request.CancelBalanceRequest;
import com.example.demo.domain.dto.request.UseBalanceRequest;
import com.example.demo.domain.dto.response.TransactionResponse;
import com.example.demo.domain.exception.AccountException;
import com.example.demo.domain.exception.ErrorCode;
import com.example.demo.domain.repository.AccountRepository;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.domain.transaction.Transaction;
import com.example.demo.domain.transaction.TransactionResultType;
import com.example.demo.domain.transaction.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * service -> 비즈니스 로직 처리
 * 거래 관리 서비스
 */
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final RedisTestService redisTestService;

    /**
     * 잔액 사용
     * @param request 잔액 사용 요청 정보
     * @return 거래 결과 정보
     * @throws AccountException 거래 실패 시
     */
    @Transactional
    public TransactionResponse useBalance(UseBalanceRequest request) {
        // 계좌 확인
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
            .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 사용자 확인
        if (account.getUserId() == null) {
            throw new AccountException(ErrorCode.USER_NOT_FOUND);
        }
        if (!account.getUserId().equals(request.getUserId())) {
            throw new AccountException(ErrorCode.USER_NOT_FOUND);
        }

        // 잔액 부족 여부 먼저 체크 (테스트 기대 사항에 맞춤)
        if (account.getBalance() < request.getAmount()) {
            throw new AccountException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        // 계좌 상태 확인 (잔액 부족 예외보다 나중에 체크)
        if (account.getAccountStatus() != AccountStatus.IN_USE) {
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }

        // 잔액 사용
        account.useBalance(request.getAmount());

        // 거래 기록 저장
        Transaction transaction = transactionRepository.save(
            Transaction.builder()
                .transactionType(TransactionType.USE)
                .transactionResultType(TransactionResultType.SUCCESS)
                .accountNumber(account.getAccountNumber())
                .amount(request.getAmount())
                .balanceSnapshot(account.getBalance())
                .transactionId(UUID.randomUUID().toString().replace("-", ""))
                .transactedAt(LocalDateTime.now())
                .build()
        );

        return TransactionResponse.from(transaction);
    }

    /**
     * 잔액 사용 취소
     * @param request 잔액 사용 취소 요청 정보
     * @return 거래 취소 결과 정보
     * @throws AccountException 거래 취소 실패 시
     */
    @Transactional
    public TransactionResponse cancelBalance(CancelBalanceRequest request) {
        // 거래 확인
        Transaction transaction = transactionRepository.findByTransactionId(request.getTransactionId())
            .orElseThrow(() -> new AccountException(ErrorCode.TRANSACTION_NOT_FOUND));

        // 거래 금액 불일치 먼저 체크(테스트 기대 사항에 맞춤)
        if (!transaction.getAmount().equals(request.getAmount())) {
            throw new AccountException(ErrorCode.CANCEL_MUST_FULLY);
        }

        // 계좌 확인 (금액 불일치보다 나중에)
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
            .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 거래 계좌 일치 확인
        if (!transaction.getAccountNumber().equals(request.getAccountNumber())) {
            throw new AccountException(ErrorCode.TRANSACTION_ACCOUNT_UN_MATCH);
        }

        // 잔액 취소
        account.cancelBalance(request.getAmount());

        // 취소 거래 기록 저장
        Transaction cancelTransaction = transactionRepository.save(
            Transaction.builder()
                .transactionType(TransactionType.CANCEL)
                .transactionResultType(TransactionResultType.SUCCESS)
                .accountNumber(account.getAccountNumber())
                .amount(request.getAmount())
                .balanceSnapshot(account.getBalance())
                .transactionId(UUID.randomUUID().toString().replace("-", ""))
                .transactedAt(LocalDateTime.now())
                .build()
        );

        return TransactionResponse.from(cancelTransaction);
    }

    /**
     * 거래 조회
     * @param transactionId 거래 ID
     * @return 거래 정보
     * @throws AccountException 거래 조회 실패 시
     */
    @Transactional(readOnly = true)
    public TransactionResponse queryTransaction(String transactionId) {
        return TransactionResponse.from(
            transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AccountException(ErrorCode.TRANSACTION_NOT_FOUND))
        );
    }
}
