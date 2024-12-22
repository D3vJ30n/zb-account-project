package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI; // OpenAPI 클래스는 Swagger 3.0 스펙을 정의하는 데 사용
import io.swagger.v3.oas.models.info.Info; // API 정보(title, description, version 등)를 정의하는 클래스
import org.springframework.context.annotation.Bean; // Bean 등록을 위한 어노테이션
import org.springframework.context.annotation.Configuration; // Spring의 Configuration 클래스임을 나타냄

/**
 * config -> 설정 파일
 * SwaggerConfig 클래스는 OpenAPI 3.0 기반의 Swagger 설정을 담당
 * Swagger는 REST API 문서를 자동으로 생성해주는 도구이며, API 명세를 시각적으로 제공
 */
@Configuration // 이 어노테이션은 해당 클래스가 Spring의 Configuration 클래스임을 나타냄
public class SwaggerConfig {

    /**
     * OpenAPI Bean을 생성하여 Spring Context에 등록
     * OpenAPI 객체는 API 명세를 설정하는 데 사용
     *
     * @return OpenAPI 객체
     */
    @Bean // 이 메서드가 반환하는 OpenAPI 객체를 Spring Bean으로 등록
    public OpenAPI openAPI() {
        // OpenAPI 객체 생성 및 설정
        return new OpenAPI()
            // .info() 메서드를 통해 API 정보 설정
            .info(new Info()
                .title("Account System API") // API의 제목 설정
                .description("계좌 관리 시스템 API 명세") // API의 설명 설정
                .version("1.0.0")); // API의 버전 설정
    }
}
