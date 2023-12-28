package org.choongang.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * 스프링 3.0 버전임 -> 메서드 체인
     * 2.0과 다름
     * 도메인 설정 언어로 dsl? dsn?으로 되어있음 영역별...
     * 람다식이 축약되어있음
     * 3.1 -> 람다식 축약 & dsl 언어
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.build();
    }
}
