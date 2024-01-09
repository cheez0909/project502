package org.choongang.board.entites;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import lombok.Data;
import org.choongang.commons.entites.BaseMember;
import org.choongang.member.entities.Member;

@Data
@Entity
public class BoardData extends BaseMember {

    @Id @GeneratedValue
    private Long seq;
    private String subject;
    private String content;

    @ManyToOne
    @JoinColumn(name = "boardData_id")
    private Member member;
}
