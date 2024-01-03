package org.choongang.commons.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CommonInterceptor implements HandlerInterceptor {
    /**
     * 공통적인 처리만 할 것? (240102 / 5교시 초반)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        checkDevice(request);
        clearLoginData(request);
        return true;
    }

    /**
     * PC, 모바일 수동 변경 처리
     * device - PC : PC 뷰, Mobile : Mobile 뷰
     */
    private void checkDevice(HttpServletRequest request){
        String device = request.getParameter("device");
        // device 값이 없을 때 처리하지 않음
        if(!StringUtils.hasText(device)){
            return;
        }

        device = device.toUpperCase().equals("MOBILE") ? "MOBILE" : "PC";
        HttpSession session = request.getSession();
        session.setAttribute("device", device);
    }

    private void clearLoginData(HttpServletRequest request){
        // 세션을 지움(로그인 후에 다른페이지 갔다가 member/login 으로 오면 메세지가 남아있음
        // 로그인 후엔 로그인메세지 정보가 담긴 세션을 지운다.
        String requestURI = request.getRequestURI();
        // 특정 문자열이 있는지 indexof
        if(requestURI.indexOf("/member/login") == -1){
            HttpSession session = request.getSession();
            MemberUtil.clearLoginData(session);
        }
    }
}
