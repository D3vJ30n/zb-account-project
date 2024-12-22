package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * config -> 설정 파일
 * EmbeddedRedisConfig 클래스는 테스트 환경에서 사용되는 내장 Redis 서버를 설정하는 클래스
 */

@Slf4j
@Configuration
@Profile("test")
public class EmbeddedRedisConfig {

    // Redis 서버의 포트 번호
    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    private RedisServer redisServer;

    // 사용 가능한 포트 찾기
    @PostConstruct
    public void startRedis() throws IOException {
        // 포트가 이미 사용 중인 경우 다른 포트 찾기
        redisPort = findAvailablePort();

        // Redis 서버 시작
        try {
            redisServer = RedisServer.builder()
                .port(redisPort)
                .setting("maxmemory 128M") // 메모리 제한 이유 : 메모리 부족 방지
                .build();
            redisServer.start();

            // 시작 메시지 출력
            log.info("Embedded Redis Server started on port {}", redisPort);
        } catch (Exception e) {
            log.error("Redis Server 시작 중 오류 발생", e);
            throw new RuntimeException("Redis Server를 시작할 수 없습니다.", e);
        }
    }

    // 서버 종료 시 실행되는 메서드
    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
            log.info("Embedded Redis Server stopped");
        }
    }

    // 사용 가능한 포트 찾기
    private int findAvailablePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}