package org.choongang.board.repositories;

import org.choongang.board.entites.QSaveBoardData;
import org.choongang.board.entites.SaveBoardData;
import org.choongang.board.entites.SaveBoardDataId;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

public interface SaveBoardDataRepository extends JpaRepository<SaveBoardData, SaveBoardDataId>, QuerydslPredicateExecutor<SaveBoardData> {

    /**
     * 회원별로 게시글 번호를 가져올 수 있게
     */
    default List<Long> getBoardDataSeqs(Long mSeq){
        QSaveBoardData saveBoardData = QSaveBoardData.saveBoardData;

        List<SaveBoardData> items = (List<SaveBoardData>) findAll(saveBoardData.mSeq.eq(mSeq), Sort.by(desc("createdAt")));
        return items.stream().map(SaveBoardData::getBSeq).toList();
    }
}
