package com.example.demo.service;

import com.example.demo.domain.account.Account;
import com.example.demo.domain.account.AccountStatus;
import com.example.demo.domain.dto.request.CancelBalanceRequest;
import com.example.demo.domain.dto.request.UseBalanceRequest;
import com.example.demo.domain.exception.AccountException;
import com.example.demo.domain.exception.ErrorCode;
import com.example.demo.domain.repository.AccountRepository;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.domain.transaction.Transaction;
import com.example.demo.domain.transaction.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("잔액 사용 성공")
    void useBalanceSuccess() {
        // given
        given(accountRepository.findByAccountNumber(anyString()))
            .willReturn(Optional.of(Account.builder()
                .accountNumber("1000000000")
                .userId(1L)
                .balance(10000L)
                .accountStatus(AccountStatus.IN_USE)
                .build()));

        given(transactionRepository.save(any()))
            .willReturn(Transaction.builder()
                .accountNumber("1000000000")
                .amount(1000L)
                .transactionType(TransactionType.USE)
                .build());

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);

        // when
        transactionService.useBalance(new UseBalanceRequest(1L, "1000000000", 1000L));

        // then
        verify(transactionRepository, times(1)).save(captor.capture());
        assertEquals(1000L, captor.getValue().getAmount());
        assertEquals(TransactionType.USE, captor.getValue().getTransactionType());
    }

    @Test
    @DisplayName("잔액 사용 실패 - 잔액 부족")
    void useBalance_insufficientBalance() {
        // given
        given(accountRepository.findByAccountNumber("1000000000"))
            .willReturn(Optional.of(Account.builder()
                .accountNumber("1000000000")
                .userId(1L)
                .balance(50L)
                .build()));

        // when & then
        AccountException exception = assertThrows(AccountException.class, () ->
            transactionService.useBalance(new UseBalanceRequest(1L, "1000000000", 100L)));

        // 수정 전
        // assertEquals(ErrorCode.AMOUNT_EXCEED_BALANCE, exception.getErrorCode());

        // 수정 후
        assertEquals(ErrorCode.INSUFFICIENT_BALANCE, exception.getErrorCode());
    }


    @Test
    @DisplayName("거래 취소 성공")
    void cancelBalanceSuccess() {
        // given
        // Account 생성 시 userId 설정
        Account account = Account.builder()
            .accountNumber("1000000000")
            .userId(1L) // 이 부분이 null이면 오류 발생
            .balance(50L)
            .build();

        given(transactionRepository.findByTransactionId(anyString()))
            .willReturn(Optional.of(Transaction.builder()
                .accountNumber("1000000000")
                .transactionType(TransactionType.USE)
                .amount(1000L)
                .build()));

        given(accountRepository.findByAccountNumber(anyString()))
            .willReturn(Optional.of(account));

        given(transactionRepository.save(any()))
            .willReturn(Transaction.builder()
                .accountNumber("1000000000")
                .amount(1000L)
                .transactionType(TransactionType.CANCEL)
                .build());

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);

        // when
        transactionService.cancelBalance(
            new CancelBalanceRequest("transactionId", "1000000000", 1000L));

        // then
        verify(transactionRepository, times(1)).save(captor.capture());
        assertEquals(1000L, captor.getValue().getAmount());
        assertEquals(TransactionType.CANCEL, captor.getValue().getTransactionType());
    }

    @Test
    @DisplayName("거래 취소 실패 - 거래 금액 불일치")
    void cancelBalance_amountMismatch() {
        // given
        given(transactionRepository.findByTransactionId(anyString()))
            .willReturn(Optional.of(Transaction.builder()
                .accountNumber("1000000000")
                .amount(1000L)
                .build()));

        // when & then
        AccountException exception = assertThrows(AccountException.class,
            () -> transactionService.cancelBalance(
                new CancelBalanceRequest("transactionId", "1000000000", 500L)));

        assertEquals(ErrorCode.CANCEL_MUST_FULLY, exception.getErrorCode());
    }
}