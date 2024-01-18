package org.choongang.board.service;


import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.BoardDataSearch;
import org.choongang.board.entites.BoardData;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.board.repositories.BoardRepository;
import org.choongang.file.service.FileDeleteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 바로 삭제하지 말고
@Service
@RequiredArgsConstructor
@Transactional // 안전하게 삭제하기 위해서
public class BoardDeleteService {

    private final BoardDataRepository boardDataRepository;
    private final BoardInfoService boardInfoService;
    private final FileDeleteService fileDeleteService;
    private final BoardAuthService boardAuthService;

    /**
     * 게시글 삭제
     *
     * @param seq
     */
    public void delete(Long seq){
        // 삭제 권한 체크
        boardAuthService.check("delete", seq);

        BoardData data = boardInfoService.get(seq);

        String gid = data.getGid();

        boardDataRepository.delete(data);
        boardDataRepository.flush();

        // 업로드 된 파일 삭제
        fileDeleteService.delete(gid);
    }
}

