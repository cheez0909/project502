package org.choongang.member.repositories;

import org.choongang.member.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

    /**
     * 반환값이 optional -> 회원정보가 없을 땐 없다고 하려고..
     */
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserId(String userId);
}
