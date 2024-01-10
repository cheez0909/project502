package org.choongang.board.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.choongang.commons.entites.BaseMember;

@Data
@Entity
public class BoardData extends BaseMember {

    @Id @GeneratedValue
    private Long seq;
    private String subject;
    private String content;

//    @ManyToOne
//    @JoinColumn(name = "boardData_id")
//    private Member member;
}
