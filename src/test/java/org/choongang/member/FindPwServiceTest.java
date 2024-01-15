package org.choongang.member;


import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.choongang.member.services.FindPwService;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class FindPwServiceTest {

    @Autowired
    private FindPwService service;


    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Disabled
    @DisplayName("비밀번호 초기화 및 초기화된 메일 이메일 전송 테스트")
    void resetTest() {
        assertDoesNotThrow(() -> service.reset("bin0696@naver.com"));
    }

    @Test
    @DisplayName("회원추가")
    @WithMockUser
    void addMember() {
        for (int i = 1; i < 100; i++) {
            Member member = Member.builder()
                    .name("사용자" + i).password("123456")
                    .email("e" + i + "@naver.com")
                    .userId("id" + i).gid(UUID.randomUUID().toString())
                    .build();
            memberRepository.saveAndFlush(member);
        }
    }
}


