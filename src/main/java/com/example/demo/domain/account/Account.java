package com.example.demo.domain.account;

import com.example.demo.domain.exception.AccountException;
import com.example.demo.domain.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * domain -> 엔티티 클래스 정의
 * 계좌 정보를 관리하는 엔티티 클래스, 엔티티는 테이블과 1:1 매핑되는 클래스, 데이터베이스의 테이블을 나타냄
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Account {
    /**
     * 계좌 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 계좌번호 (10자리 고유번호)
     */
    @Column(unique = true, nullable = false, length = 10)
    private String accountNumber;

    /**
     * 계좌 소유자 ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 계좌 잔액
     */
    @Column(nullable = false)
    private Long balance;

    /**
     * 계좌 상태 (IN_USE, UNREGISTERED)
     */
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    /**
     * 계좌 등록일시
     */
    private LocalDateTime registeredAt;

    /**
     * 계좌 해지일시
     */
    private LocalDateTime unregisteredAt;

    /**
     * 잔액 사용 처리
     * @param amount 사용할 금액
     * @throws AccountException 잔액이 부족한 경우 발생
     */
    public void useBalance(Long amount) {
        if (amount > this.balance) {
            throw new AccountException(ErrorCode.BALANCE_NOT_EMPTY);
        }
        this.balance -= amount;
    }

    /**
     * 잔액 취소 처리 (환불)
     * @param amount 취소할 금액
     */
    public void cancelBalance(Long amount) {
        balance += amount;
    }

    /**
     * 계좌 해지 처리
     * @throws IllegalArgumentException 잔액이 있는 경우 발생
     */
    public void unregister() {
        if (balance > 0) {
            throw new IllegalArgumentException("잔액이 있는 계좌는 해지할 수 없습니다.");
        }
        accountStatus = AccountStatus.UNREGISTERED;
        unregisteredAt = LocalDateTime.now();
    }
}