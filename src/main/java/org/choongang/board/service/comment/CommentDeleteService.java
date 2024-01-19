package org.choongang.board.service.comment;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entites.CommentData;
import org.choongang.board.repositories.CommentDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDeleteService {

    private final CommentInfoService commentInfoService;
    private final CommentDataRepository commentDataRepository;



    public Long delete(Long seq){

        CommentData data = commentInfoService.get(seq);
        // 삭제 시에 null이 뜰 수 있음
        Long boardDataSeq = data.getBoardData().getSeq();

        commentDataRepository.delete(data);
        commentDataRepository.flush();

        return boardDataSeq;
    }
    /**
     * 댓글 저장, 수정 처리
     *
     * @param seq
     * @return
     */
//    public CommentData delete(Long seq){
//
//        CommentData data = commentInfoService.get(seq);
//        // 삭제 시에 null이 뜰 수 있음
//        CommentData returnData = new ModelMapper().map(data, CommentData.class);
//
//        commentDataRepository.delete(data);
//        commentDataRepository.flush();
//
//        return returnData;
//    }

}
