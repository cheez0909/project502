package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.Data;

import org.choongang.commons.entites.Base;

@Data
@Entity
@IdClass(Follower.class)
public class Follower extends Base{

    @Id
    private Long seq; // following 회원 번호

    @Id
    @JoinColumn(name = "memberSeq")
    @ManyToOne
    private Member member;

}
