package org.choongang.configs;

import org.choongang.member.services.LoginFailureHandler;
import org.choongang.member.services.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

        /** 인증 설정  S - 로그인 **/
        // 보통 필터체인 메서드를 사용하여 인증기능을 구현함
        // 보다 상세하게 설정이 필요할 경우 핸들러가 필요
        http.formLogin(f->{
            // 설정할 때 바뀌는 부분만 우리가 설정함(알려줘야함 시큐리티한테)
            // ( 아이디, 비밀번호 같은것들 )
           f.loginPage("/member/login") // 상황에 따라 달라지는 것은 상세설정 가능
                   .usernameParameter("username") // 아이디 일 수도 이메일 일 수 도 있음.. 이런부분 상세설정가능
                   .passwordParameter("password") // 뷰에 name값을 설정해줌
                   // .defaultSuccessUrl("/") // 로그인 성공시 메인 페이지
                   .successHandler(new LoginSuccessHandler()) // 핸들러로 설정!!
                   // .failureUrl("/member/login?error=true"); // 실패시 이동할 url
                   .failureHandler(new LoginFailureHandler());
        });
        /** 인증 설정  E - 로그인 **/

        return http.build();
    }

    /**
     * 비밀번호 해시화
     * PasswordEncoder -> 인터페이스
     * BCryptPasswordEncoder -> 구현체 / 여러가지가 있음.
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
