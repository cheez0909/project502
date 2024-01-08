package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.Utils;
import org.choongang.email.service.EmailMessage;
import org.choongang.email.service.EmailSendService;
import org.choongang.member.controllers.FindIdValidator;
import org.choongang.member.controllers.RequestFindId;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FindIdService {
    private final FindIdValidator validator;
    private final MemberRepository repository;
    private final EmailSendService sendService;
    private final PasswordEncoder encoder;
    private final Utils utils;

    public void process(RequestFindId form, Errors errors) {
        validator.validate(form, errors);
        if (errors.hasErrors()) { // 유효성 검사 실패시에는 처리 중단
            return;
        }

        // 비밀번호 초기화
        // showId(form.email());
    }

    public void reset(String email) {

    }
}
