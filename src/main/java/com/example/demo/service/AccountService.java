package com.example.demo.service;

import com.example.demo.domain.account.Account;
import com.example.demo.domain.account.AccountStatus;
import com.example.demo.domain.dto.request.CreateAccountRequest;
import com.example.demo.domain.dto.request.DeleteAccountRequest;
import com.example.demo.domain.dto.response.AccountResponse;
import com.example.demo.domain.exception.AccountException;
import com.example.demo.domain.exception.ErrorCode;
import com.example.demo.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * service -> 비즈니스 로직 처리
 * 계좌 관리 서비스
 */
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final RedisTestService redisTestService;

    /**
     * 계좌 생성
     * @param request 계좌 생성 요청 정보
     * @return 생성된 계좌 정보
     * @throws AccountException 계좌 생성 실패 시
     */
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        // 계좌 수 확인
        if (accountRepository.findByUserId(request.getUserId()).size() >= 10) {
            throw new AccountException(ErrorCode.MAX_ACCOUNT_PER_USER_10);
        }

        // 계좌번호 생성
        String newAccountNumber;
        do {
            newAccountNumber = String.format("%010d", new Random().nextInt(1000000000));
        } while (accountRepository.existsByAccountNumber(newAccountNumber));

        // 계좌 생성
        Account account = accountRepository.save(
            Account.builder()
                .accountNumber(newAccountNumber)
                .userId(request.getUserId())
                .balance(request.getInitialBalance())
                .accountStatus(AccountStatus.IN_USE)
                .registeredAt(LocalDateTime.now())
                .build()
        );

        return AccountResponse.from(account);
    }

    /**
     * 계좌 해지
     * @param request 계좌 해지 요청 정보
     * @return 해지된 계좌 정보
     * @throws AccountException 계좌 해지 실패 시
     */
    @Transactional
    public AccountResponse deleteAccount(DeleteAccountRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
            .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 사용자 일치 확인
        if (!account.getUserId().equals(request.getUserId())) {
            throw new AccountException(ErrorCode.USER_NOT_FOUND);
        }

        // 계좌 상태 확인
        if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }

        // 잔액 확인
        if (account.getBalance() > 0) {
            throw new AccountException(ErrorCode.BALANCE_NOT_EMPTY);
        }

        account.unregister(); // 상태 변경
        accountRepository.save(account); // @Transactional이 설정된 메서드에서는 엔티티의 상태 변화가 자동으로 감지. 하지만 Mock 객체를 사용한 테스트에서는 이를 모의(Mock)하기 때문에 save()가 호출되지 않은 것으로 인식됨. 따라서 명시적으로 save()를 호출하여 엔티티의 상태 변화를 저장해야 함.

        return AccountResponse.from(account);
    }


    /**
     * 사용자의 계좌 목록 조회
     * @param userId 사용자 ID
     * @return 계좌 목록
     */
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId).stream()
            .map(AccountResponse::from)
            .collect(Collectors.toList());
    }
}
