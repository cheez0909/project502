package org.choongang.member.services.service;

import lombok.RequiredArgsConstructor;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Follower;
import org.choongang.member.entities.Member;
import org.choongang.member.entities.QFollow;
import org.choongang.member.entities.QFollower;
import org.choongang.member.repositories.FollowerRepository;
import org.choongang.member.repositories.FollowingRepository;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowerRepository followerRepository;
    private final MemberUtil memberUtil;
    private final FollowingRepository followingRepository;
    private final MemberRepository memberRepository;


    /**
     * 내가 팔로잉함
     * @param mSeq
     */
    public void following(Long mSeq){
        if(!memberUtil.isLogin()){
            return;
        }
        Member member = memberUtil.getMember();

        Follower following = new Follower();
        following.setSeq(mSeq);
        following.setMember(member);

        followingRepository.saveAndFlush(following);

        Member followerMember = memberRepository.findById(mSeq).orElse(null);

        Follower follower = new Follower();
        follower.setSeq(member.getSeq());
        follower.setMember(followerMember);
        followerRepository.saveAndFlush(follower);
    }

    public List<Member> getFollowers(Long Seq){
        QFollower follower = QFollower.follower;

        List<Follwer> memvers = follower
    }
}
