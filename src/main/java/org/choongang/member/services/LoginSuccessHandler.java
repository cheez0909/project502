package org.choongang.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 상세한 설정이 필요할 때
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 로그인 회원 정보가 담겨 있는 객체 -> Authentication
     * 성공 시에 로그인 회원정보가 같이 넘어옴!
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        MemberUtil.clearLoginData(session);

        /** 회원정보를 세션에 담아서 활용할 수 있다!
         * SecurityContextHolder 를 활용할 때는 .getContext.getAuthentication을 활용해야함...
         **/
        MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();
        Member member = memberInfo.getMember();
        session.setAttribute("member", member);

        String redirectURL = request.getParameter("redirectURL");
        redirectURL = StringUtils.hasText(redirectURL) ? redirectURL : "/";
        response.sendRedirect(request.getContextPath()+redirectURL);
    }
}
