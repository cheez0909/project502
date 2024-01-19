package org.choongang.board.repositories;

import org.choongang.board.entites.CommentData;
import org.choongang.board.entites.QCommentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentDataRepository extends JpaRepository<CommentData, Long>, QuerydslPredicateExecutor<CommentData> {

    /**
     * 게시글 별 댓글 개수
     *  - 게시글 데이터에 업데이트 바로해주면 됨
     *
     * @param boardDataSeq
     * @return
     */
    default int getTotal(Long boardDataSeq){
        QCommentData commentData = QCommentData.commentData;

        return (int) count(commentData.boardData.seq.eq(boardDataSeq));
    }
}
