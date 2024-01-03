package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.member.Authority;
import org.choongang.member.controllers.JoinValidator;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.AuthoritiesRepository;
import org.choongang.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
@Transactional // 연달아 추가할 수도 있기 때문에
public class JoinService {

    private final MemberRepository memberRepository;
    private final JoinValidator joinValidator; // 유효성 검사
    private final PasswordEncoder encoder;
    private final AuthoritiesRepository authoritiesRepository;

    /**
     * 비밀번호 검증
     * @param form
     * @param errors
     */
    public void process(RequestJoin form, Errors errors){
        joinValidator.validate(form, errors);
        // 검증 실패시 종료
        if(errors.hasErrors()){
            return;
        }
        // 비밀번호 해시화
        Member member = new ModelMapper().map(form, Member.class);
        String password = encoder.encode(form.getPassword());
        member.setPassword(password);
        process(member);

        // 회원 가입 시에는 일반 사용자 권한 부여(USER)
        Authorities authorities = new Authorities();
        authorities.setMember(member);
        authorities.setAuthority(Authority.USER);
        authoritiesRepository.saveAndFlush(authorities);
    }

    /**
     * 회원 가입
     * @param member
     */
    public void process(Member member){
        memberRepository.save(member);
    }
}
