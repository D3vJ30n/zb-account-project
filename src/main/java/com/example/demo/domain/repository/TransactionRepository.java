package com.example.demo.domain.repository;

import com.example.demo.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * repository -> 데이터 접근 계층
 * 거래 정보 레포지토리
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * 거래 ID로 거래 정보 조회
     * @param transactionId 거래 ID
     * @return 거래 정보
     */
    Optional<Transaction> findByTransactionId(String transactionId);
}
