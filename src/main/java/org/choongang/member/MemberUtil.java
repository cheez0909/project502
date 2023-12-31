package org.choongang.member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final HttpSession session;

    // 최고 관리자 인지 확인
    public boolean isAdmin(){
        if(isLogin()){
            return getMember().getAuthorities()
                    .stream().map(Authorities::getAuthority)
                    .anyMatch(a->a == Authority.ADMIN || a == Authority.MANAGER); // 하나만 매칭돼도 참
        }
        return false;
    }

    /**
     * 세션에 회원정보가 있다면 로그인 상태이다
     */
    public boolean isLogin(){
        return getMember()!=null;
    }
    public Member getMember(){
        Member member = (Member)session.getAttribute("member");
        return member;
    }

    /**
     * 로그인 실패 or 성공 시
     * 세션 초기화 작업
     */
    public static void clearLoginData(HttpSession session){
        session.removeAttribute("username");
        session.removeAttribute("NotBlank_username");
        session.removeAttribute("NotBlank_password");
        session.removeAttribute("Global_error");
    }


}
