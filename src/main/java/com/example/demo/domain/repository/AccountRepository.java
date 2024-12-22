package com.example.demo.domain.repository;

import com.example.demo.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * repository -> 데이터 접근 계층
 * 계좌 정보 레포지토리
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * 계좌번호로 계좌 조회
     * @param accountNumber 계좌번호
     * @return 계좌 정보
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * 사용자 ID로 계좌 목록 조회
     * @param userId 사용자 ID
     * @return 계좌 목록
     */
    List<Account> findByUserId(Long userId);

    /**
     * 계좌번호 존재 여부 확인
     * @param accountNumber 계좌번호
     * @return 존재 여부
     */
    boolean existsByAccountNumber(String accountNumber);
}
