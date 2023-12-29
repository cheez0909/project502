package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.http.HttpRequest;

@Component // 스프링 관리 객체
@RequiredArgsConstructor
public class Utils {

    /**
     * 매개변수에 넣지 않은 이유
     * 제공된 코드에서는 클래스 private final HttpServletRequest request;에
     * 인스턴스 변수로 가 선언되어 있으며 @RequiredArgsConstructor를 통해 생성자 주입을 통해 주입되고 있습니다
     *  이는 클래스 HttpServletRequest의 인스턴스가 Utils생성될 때 객체가
     *  제공될 것으로 예상된다는 것을 의미합니다.
     */
    private final HttpServletRequest request;
    private final HttpSession session;

    /**
     * 모바일과 pc 구분하기
     */
    public boolean isMobile(){
        /**
         * 모바일 수동 전환 모드 체크
         * 값이 있으면 모바일 없으면 PC
         */
        String device = (String) session.getAttribute("device");
        if(StringUtils.hasText(device)){
            return device.equals("MOBILE");
        }

        /**
         * 요청헤더에서 user-Agent 정보 불러오기
         */
        String ua = request.getHeader("User-Agent");

        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*";

        return ua.matches(pattern);
    }

    /**
     * 경로 설정
     */
    public String tpl(String path) {
        String prefix = isMobile() ? "mobile/" : "front/";

        return prefix + path;
    }
}