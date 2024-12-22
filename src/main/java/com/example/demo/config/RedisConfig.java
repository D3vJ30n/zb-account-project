package com.example.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * config -> 설정 파일
 * RedisConfig 클래스는 Redis와 연동하기 위한 설정을 정의
 * 이 클래스는 Redisson 라이브러리를 사용하여 Redis 클라이언트를 생성하며,
 * Spring Context에서 관리되는 Bean으로 등록
 */
@Configuration
@Profile("!test")  // 테스트 환경이 아닐 때만 사용
public class RedisConfig {
    /**
     * Redis 서버의 호스트 주소를 설정 파일(application.yml)에서 가져옴
     */

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    /**
     * RedisConnectionFactory 객체를 생성하여 Bean으로 등록
     * 이 Bean은 Redis와의 상호작용을 가능하게 하는 클라이언트로 사용
     *
     * @return RedisConnectionFactory 인스턴스
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    /**
     * RedisTemplate 객체를 생성하여 Bean으로 등록
     * 이 Bean은 Redis와의 상호작용을 가능하게 하는 클라이언트로 사용
     *
     * @return RedisTemplate 인스턴스
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    /**
     * RedissonClient 객체를 생성하여 Bean으로 등록
     * 이 Bean은 Redis와의 상호작용을 가능하게 하는 클라이언트로 사용
     *
     * @return RedissonClient 인스턴스
     */
    @Bean
    public RedissonClient redissonClient() {
        // Redisson의 Config 객체를 생성한다. 이는 Redisson 클라이언트 설정 정보를 포함
        Config config = new Config();

        // Redis 서버를 단일 서버 모드로 설정
        // setAddress 메서드는 Redis 서버의 주소를 "redis://<호스트>:<포트>" 형식으로 지정
        config.useSingleServer()
            .setAddress("redis://" + redisHost + ":" + redisPort);

        // 생성된 Config 객체를 기반으로 RedissonClient 인스턴스를 생성하여 반환
        // Redisson.create() 메서드는 내부적으로 연결 풀을 구성하고 Redis 서버와 통신 가능하도록 클라이언트를 초기화
        return Redisson.create(config);
    }
}
