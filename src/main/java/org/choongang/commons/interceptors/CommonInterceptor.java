package org.choongang.commons.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CommonInterceptor implements HandlerInterceptor {
    /**
     * 공통적인 처리만 할 것? (5교시 초반)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        checkDevice(request);
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
}
