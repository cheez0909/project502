package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final MemberRepository memberRepository;

    public void process(){

    }
}
