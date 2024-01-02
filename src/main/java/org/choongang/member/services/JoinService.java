package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.member.controllers.JoinValidator;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final JoinValidator joinValidator; // 유효성 검사
    private final PasswordEncoder encoder;

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
    }

    /**
     * 회원 가입
     * @param member
     */
    public void process(Member member){
        memberRepository.save(member);
    }
}
