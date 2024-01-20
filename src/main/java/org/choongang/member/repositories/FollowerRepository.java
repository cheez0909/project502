package org.choongang.member.repositories;

import org.choongang.member.entities.Follow;
import org.choongang.member.entities.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FollowerRepository extends JpaRepository<Follow, FollowId>, QuerydslPredicateExecutor<Follow> {
}
