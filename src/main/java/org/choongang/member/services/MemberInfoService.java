package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.entites.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final FileInfoService fileInfoService;
    /**
     * 반환값이 UserDetails 임 -> 구현체로서 반환값만 넣어주면 됨
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username) // 이메일조회
                .orElseGet(()->memberRepository.findByUserId(username) // 아이디 조회
                        .orElseThrow(()-> new UsernameNotFoundException(username))); // 예외

        List<Authorities> tmp = member.getAuthorities();
        List<SimpleGrantedAuthority> authorities = null;
        // map은 변환 메서드
        // 권한이 문자열이기때문에
        // i.getAuthority().name() : enum상수 그대로 인식할 수 없기 때문에 문자열로넣어주어야함!
        if(tmp != null){
            authorities = tmp.stream()
                    .map(i-> new SimpleGrantedAuthority(i.getAuthority().name()))
                    .toList();
        }

        /* 프로필 이미지 처리 S */
        List<FileInfo> files = fileInfoService.getListDone(member.getGid());
        if(files !=null && !files.isEmpty()){
            member.setProfileImage(files.get(0));
        }
        /* 프로필 이미지 처리 E */

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .userId(member.getUserId())
                .member(member)
                .authorities(authorities)
                .build();
    }
}
