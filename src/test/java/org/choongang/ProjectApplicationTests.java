package org.choongang;

import org.choongang.member.Authority;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.AuthoritiesRepository;
import org.choongang.member.repositories.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// @TestPropertySource(properties = "spring.profiles.active=test")
class ProjectApplicationTests {

	@Autowired
	private AuthoritiesRepository authoritiesRepository;
	@Autowired
	private MemberRepository memberRepository;

	@Test @Disabled
	void contextLoads() {
		Member member = memberRepository.findByUserId("user01").orElse(null);

		Authorities authorities = new Authorities();
		authorities.setMember(member);
		authorities.setAuthority(Authority.ADMIN);

		authoritiesRepository.saveAndFlush(authorities);
	}

}
