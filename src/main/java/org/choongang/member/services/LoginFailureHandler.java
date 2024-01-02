package org.choongang.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.commons.Utils;
import org.choongang.member.MemberUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 상세하게 설정하고 싶을 때
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    /**
     * AuthenticationException -> 인증 실패 시, 예외 객체
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpSession session = request.getSession();

        // 세션 로그인 실패 메세지 일괄 삭제 처리
        MemberUtil.clearLoginData(session);


        String username = request.getParameter("username");
        String password = request.getParameter("password");


        if(!StringUtils.hasText(username)){
            // session.setAttribute("NotBlank_userId", "아이디를 입력하세요");
            session.setAttribute("NotBlank_userId", Utils.getMessage("NotBlank.userId"));
        }

        if(!StringUtils.hasText(password)){
            session.setAttribute("NotBlank_password", Utils.getMessage("NotBlank.password"));
        }

        session.setAttribute("username", username);
        session.setAttribute("password", password);

        // 아이디, 비번이 있지만 실패한 경우 -> 회원 정보가 일치하지 않을 때
        if(StringUtils.hasText(username) && StringUtils.hasText(password)){
            session.setAttribute("Global_error", Utils.getMessage("Fail.login", "errors"));
        }



        // 뷰의 속성 값이 유지되지 않음
        // sendRedirect일 경우 데이터가 유지되지 않음
        // 세션에 데이터를 저장해야함.. 세션이나 어플리케이션
        // 인증 실패 시 로그인 페이지로 이동
        response.sendRedirect(request.getContextPath()+"/member/login");
    }
}
