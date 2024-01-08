package org.choongang.board.entites;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import lombok.Data;
import org.choongang.commons.entites.BaseMember;

@Data
@Entity
public class BoardData extends BaseMember {

    @Id @GeneratedValue
    private Long seq;
    private String subject;
    private String content;

}
