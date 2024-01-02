package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 반환값이 UserDetails 임 -> 구현체로서 반환값만 넣어주면 됨
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username) // 이메일조회
                .orElseGet(()->memberRepository.findByUserId(username) // 아이디 조회
                        .orElseThrow(()-> new UsernameNotFoundException(username))); // 예외


        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .userId(member.getUserId())
                .member(member)
                .build();
    }
}
