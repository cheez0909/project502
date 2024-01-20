package org.choongang.board.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;
import org.choongang.commons.entites.Base;

/**
 * 기본키 두개를 조합해서 만듦
 */
@Data
@Entity
@IdClass(SaveBoardDataId.class) // 기본키 두개를 조합해서 만들 때 필요
public class SaveBoardData extends Base {
    @Id
    private Long bSeq; // 게시글 번호
    @Id
    private Long mSeq; // 회원번호
}
