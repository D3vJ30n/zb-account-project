package com.example.demo.controller;

import com.example.demo.domain.dto.request.CreateAccountRequest;
import com.example.demo.domain.dto.request.DeleteAccountRequest;
import com.example.demo.domain.dto.response.AccountResponse;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * controller -> API 요청/응답 처리
 * 계좌 관리 API를 처리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    /**
     * 계좌 생성 API
     * @param request 계좌 생성 요청 정보 (사용자 ID, 초기 잔액)
     * @return 생성된 계좌 정보
     */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
        @RequestBody @Valid CreateAccountRequest request) {
        return ResponseEntity.ok(
            accountService.createAccount(request)
        );
    }

    /**
     * 계좌 해지 API
     * @param request 계좌 해지 요청 정보 (사용자 ID, 계좌번호)
     * @return 해지된 계좌 정보
     */
    @DeleteMapping
    public ResponseEntity<AccountResponse> deleteAccount(
        @RequestBody @Valid DeleteAccountRequest request) {
        return ResponseEntity.ok(
            accountService.deleteAccount(request)
        );
    }

    /**
     * 사용자의 계좌 목록 조회 API
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자의 계좌 목록
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(
        @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(
            accountService.getAccountsByUserId(userId)
        );
    }
}
