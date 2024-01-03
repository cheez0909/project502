package org.choongang.member.services;

import lombok.Builder;
import lombok.Data;
import org.choongang.member.entities.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 회원정보를 넣어주면 스프링 시큐리티에서
 * 알아서 조회해줌
 */
@Data
@Builder // 기본생성자가 없어서 빌더만 넣어도 됨
public class MemberInfo implements UserDetails {

    // 기본정보
    private String email;
    private String userId;
    private String password;
    // 추가적인 정보가 필요할 때
    private Member member;

    private Collection<? extends  GrantedAuthority> authorities;

    /**
     * 권한 -> 인가를 승인하게 됨(관리자는 관리자, 로그인 시 접근가능한 페이지)
     * 값을 넣어주면 알아서 조회...
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 비밀번호, 아이디(이메일, 아이디 2개로 할 예정)
     */
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return StringUtils.hasText(email) ? email : userId;
    }

    /**
     * 계정이 만료되었는지
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨있는지
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 기본이 만료되지 않은 형태인지
     * 비밀번호를 주기적으로 바꾸어주어야 할 떄 (30일이 경과되었음...)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 탈퇴와 관련된 기능
     * false일 경우 막혀서 로그인이 안됨
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
