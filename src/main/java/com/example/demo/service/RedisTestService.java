package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * service -> 비즈니스 로직 처리
 * Redis를 활용한 분산 락 서비스
 */
@Service
@RequiredArgsConstructor
public class RedisTestService {
    private final RedissonClient redissonClient;

    /**
     * 계좌에 대한 락 획득
     * @param accountNumber 계좌번호
     * @throws RuntimeException 락 획득 실패 시
     */
    public void lock(String accountNumber) {
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));
        try {
            boolean isLock = lock.tryLock(1, 15, TimeUnit.SECONDS);
            if (!isLock) {
                throw new RuntimeException("Lock acquisition failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Redis lock failed");
        }
    }

    /**
     * 계좌에 대한 락 해제
     * @param accountNumber 계좌번호
     */
    public void unlock(String accountNumber) {
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));
        try {
            lock.unlock();
        } catch (Exception e) {
            throw new RuntimeException("Redis unlock failed");
        }
    }

    /**
     * 락 키 생성
     * @param accountNumber 계좌번호
     * @return 락 키
     */
    private String getLockKey(String accountNumber) {
        return "ACLK:" + accountNumber;
    }
}
