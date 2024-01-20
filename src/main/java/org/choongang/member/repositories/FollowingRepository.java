package org.choongang.member.repositories;

import org.choongang.member.entities.Follower;
import org.choongang.member.entities.FollowerId;
import org.choongang.member.entities.QFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FollowingRepository extends JpaRepository<Follower, FollowerId>, QuerydslPredicateExecutor<Follower> {
}
