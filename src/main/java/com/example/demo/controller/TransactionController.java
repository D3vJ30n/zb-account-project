package com.example.demo.controller;

import com.example.demo.domain.dto.request.CancelBalanceRequest;
import com.example.demo.domain.dto.request.UseBalanceRequest;
import com.example.demo.domain.dto.response.TransactionResponse;
import com.example.demo.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * controller -> API 요청/응답 처리
 * 거래(Transaction) 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * 잔액 사용 API
     * @param request 잔액 사용 요청 정보 (사용자 ID, 계좌번호, 거래금액)
     * @return 거래 결과 정보
     */
    @PostMapping("/use")
    public ResponseEntity<TransactionResponse> useBalance(
        @RequestBody @Valid UseBalanceRequest request) {
        return ResponseEntity.ok(
            transactionService.useBalance(request)
        );
    }

    /**
     * 잔액 사용 취소 API
     * @param request 잔액 사용 취소 요청 정보 (거래 ID, 계좌번호, 취소금액)
     * @return 거래 취소 결과 정보
     */
    @PostMapping("/cancel")
    public ResponseEntity<TransactionResponse> cancelBalance(
        @RequestBody @Valid CancelBalanceRequest request) {
        return ResponseEntity.ok(
            transactionService.cancelBalance(request)
        );
    }

    /**
     * 거래 조회 API
     * @param transactionId 조회할 거래 ID
     * @return 거래 상세 정보
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> queryTransaction(
        @PathVariable("transactionId") String transactionId) {
        return ResponseEntity.ok(
            transactionService.queryTransaction(transactionId)
        );
    }
}