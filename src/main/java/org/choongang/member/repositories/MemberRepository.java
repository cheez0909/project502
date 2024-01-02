package org.choongang.member.repositories;

import org.choongang.member.entities.Member;
import org.choongang.member.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

    /**
     * 반환값이 optional -> 회원정보가 없을 땐 없다고 하려고..
     */
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserId(String userId);

    /**
     * 간단한 쿼리는 디폴트메서드로 정의...
     * 복잡한 기능은 서비스 하나로 묶어서 작업...
     */
    default boolean existsByEmail(String email){
        QMember member = QMember.member;
        return exists(member.email.eq(email));
    }

    default boolean existsByUserId(String userId){
        QMember member = QMember.member;
        return exists(member.userId.eq(userId));
    }
}
