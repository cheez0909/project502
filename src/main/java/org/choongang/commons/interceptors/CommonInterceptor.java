package org.choongang.commons.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.admin.config.service.ConfigInfoService;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {
    private final ConfigInfoService infoService;
    /**
     * 공통적인 처리만 할 것? (240102 / 5교시 초반)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        checkDevice(request);
        clearLoginData(request);
        loadSiteConfig(request); // 추가
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

    private void loadSiteConfig(HttpServletRequest request){
        String[] excludes = {".js", ".css", ".png", ".jpg", ".jpeg", "gif", ".pdf", ".xls", ".xlxs", ".ppt"};
        String URL = request.getRequestURI().toLowerCase(); // 대소문자 구분을 위해 소문자로 변경

        // 하나라도 매칭이 되면 안됨
        boolean isIncluded = Arrays.stream(excludes).anyMatch(s -> URL.contains(s));
        // 매칭되면 중지
        if(isIncluded){
            return;
        }
        BasicConfig config = infoService.get("basic", BasicConfig.class)
                .orElseGet(BasicConfig::new);
        request.setAttribute("siteConfig", config);
    }
}
