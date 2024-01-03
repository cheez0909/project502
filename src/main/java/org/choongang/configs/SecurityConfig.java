package org.choongang.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.choongang.member.services.LoginFailureHandler;
import org.choongang.member.services.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
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

        /** 인증 설정  S - 로그인, 로그아웃 **/
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

        http.logout(c->{
            c.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 이 url입력하면 로그아웃됨
                    .logoutSuccessUrl("/member/login"); // 이 경로로 이동
            System.out.println("미로그인상태");
        });
        /** 인증 설정  E - 로그인, 로그아웃 **/


        /** 인가 설정 S - 접근 통제 **/
        // hasAuthrity(...), hasAnyAuthority(...) / hasRole, hasAnyRole
        // ROLE_롤명칭 -> hasRole, hasAnyRole를 사용하면 ROLE을 제거하고 롤명칭만 사용됨
        // hasAuthrity('ADMIN')
        // ROLE_ADMIN -> hasRole('ADMIN')
        // authorizeHttpRequests 특정 권한이 있을때 접근가능한 페이지를 통제하는 것
        http.authorizeHttpRequests(c->{
            c.requestMatchers("/mypage/**").authenticated() // 인증된 사용자 즉, 회원 전용
                    .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "MANAGER") // 권한 있는 사람만 접근가능
                    .anyRequest().permitAll(); // 그외엔 모두 접근가능
        });
        // 상세하게 페이지 설정 가능
        // AuthenticationEntryPoint -> 람다식 형태 : 메서드가 1개임
        http.exceptionHandling(c->{
            c.authenticationEntryPoint((req, res, e) -> {
                // 현재 페이지가 관리자인지 아닌지
                String requestURI = req.getRequestURI();
                if(requestURI.indexOf("/admin")!=-1){
                    // 관리자 페이지
                    ;res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } else{
                    // 회원 전용 페이지
                    res.sendRedirect(req.getContextPath()+"/member/login");
                }
            });
        });
        /** 인가 설정 E - 접근 통제 **/

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
