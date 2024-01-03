package org.choongang.configs;

import org.choongang.member.services.MemberInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

// 스프링 관리 객체가 되어야 함
@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String userId = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 로그인 했을땐 userDetails의 구현체를 가져옴
        // 로그인하지 않았을 땐 anonymousUser(String)
        if(authentication!=null & authentication.getPrincipal() instanceof MemberInfo){
           MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();
            userId = memberInfo.getUserId();
        }
        return Optional.ofNullable(userId);
    }
}
