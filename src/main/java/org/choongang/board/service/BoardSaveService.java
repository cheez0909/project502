package org.choongang.board.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.RequestBoard;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.file.service.FileUploadService;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Service;
import org.choongang.board.entites.BoardData;
import org.springframework.util.StringUtils;

// 추가도하고 수정도 함
@Service
@RequiredArgsConstructor
public class BoardSaveService {

    private final BoardDataRepository boardDataRepository;
    private final FileUploadService fileUploadService;
    private final MemberUtil memberUtil;
    private final HttpServletRequest request;

    // 저장 후 이동할 때 게시물로 가면.. 게시물no가 필요함
    public BoardData save(RequestBoard form){
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";
        Long seq = form.getSeq();

        BoardData data = null;
        if(seq!=null && mode.equals("update")){
            // 글 수정 일때 : 글 번호가 있음
            data = boardDataRepository.findById(seq)
                    .orElseThrow(BoardDataNotFoundException::new);
        } else {
            // 글 작성 -> gid는 작성할 때만 넣어야함
            data = new BoardData();
            data.setGid(form.getGid());
            data.setIp(request.getRemoteAddr()); // ip주소
            data.setUa(request.getHeader("User-Agent")); // 브라우저
            data.setMember(memberUtil.getMember());
        }

        return data;
    }
}
