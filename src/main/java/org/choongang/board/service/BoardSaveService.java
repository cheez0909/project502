package org.choongang.board.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.RequestBoard;
import org.choongang.board.entites.Board;
import org.choongang.board.entites.QBoardData;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.board.repositories.BoardRepository;
import org.choongang.file.service.FileUploadService;
import org.choongang.member.MemberUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder encoder;
    private final BoardRepository boardRepository;
    private final BoardAuthService boardAuthService;

    // 저장 후 이동할 때 게시물로 가면.. 게시물no가 필요함
    public BoardData save(RequestBoard form){

        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";
        Long seq = form.getSeq();

        // 수정 권한 체크
        if(mode.equals("update")){
            boardAuthService.check(mode, seq);
        }

        BoardData data = null;
        if(seq!=null && mode.equals("update")){
            // 글 수정 일때 : 글 번호가 있음
            data = boardDataRepository.findById(seq)
                    .orElseThrow(BoardDataNotFoundException::new);
        } else {
            // 글 작성 -> gid는 작성할 때만 넣어야함
            data = new BoardData();

            // 처음등록할때만 등록하고 이후 변경이 되면 안됨
            data.setGid(form.getGid());
            data.setIp(request.getRemoteAddr()); // ip주소
            data.setUa(request.getHeader("User-Agent")); // 브라우저
            data.setMember(memberUtil.getMember());

            Board board = boardRepository.findById(form.getBid()).orElse(null); // 게시판을 추가해줘야함
            data.setBoard(board);
            Long parentSeq = form.getParentSeq();
            data.setParentSeq(parentSeq); // 부모 게시글 번호

            // 부모쪽에서 가져올 수 있게 1차 정렬
            long listOrder = parentSeq == null ?
                    System.currentTimeMillis() : getReplyListOrder(parentSeq);
            data.setListOrder(listOrder);

            if(parentSeq == null){ // 본글
                data.setListOrder2("R");
            } else { // 답글
                String listOrder2 = getReplyListOrder2(parentSeq);
                data.setListOrder2(listOrder2);
                int depth = StringUtils.countOccurrencesOf(listOrder2, "A");
                data.setDepth(depth);
            }

        }

        data.setPoster(form.getPoster());
        data.setSubject(form.getSubject());
        data.setContent(form.getContent());
        data.setCategory(form.getCategory());
        data.setEditorView(data.getBoard().isUseEditor()); // 에디터 일 경우



        // 추가 필드 - 정수
        data.setNum1(form.getNum1());
        data.setNum2(form.getNum2());
        data.setNum3(form.getNum3());

        // 추가 필드 - 한줄 텍스트
        data.setText1(form.getText1());
        data.setText2(form.getText2());
        data.setText3(form.getText3());

        // 추가 필드 - 여러줄 텍스트
        data.setLongText1(form.getLongText1());
        data.setLongText2(form.getLongText2());
        data.setLongText3(form.getLongText3());

        // 값이 있을때만 넣으면 됨 : 비회원 비밀번호
        String guestPw = form.getGuestPw();
        if(StringUtils.hasText(guestPw)){
            String hash = encoder.encode(guestPw);
            data.setGuestPw(hash);
        }

        // 공지글 처리 : 관리자만 가능
        if(memberUtil.isAdmin()){
            data.setNotice(form.isNotice());
        }

        boardDataRepository.saveAndFlush(data);

        // 파일 업로드 완료 처리
        fileUploadService.processDone(data.getGid());

        return data;
    }

    /**
     * 답글 정렬 순서 번호 listOrder
     *  답글의 부모 게시글의 listOrder로 대체 -> 부모게시글과 같은 라인에서 정렬이 되어야함
     *  즉, 항상 부모게시글 위에 있으면 안되고 아래에 있어야함
     *
     * @param parentSeq
     * @return
     */
    private long getReplyListOrder(Long parentSeq) {
        BoardData data = boardDataRepository.findById(parentSeq).orElse(null);
        if(data == null){
            return System.currentTimeMillis();
        }
        return data.getListOrder();
    }

    /**
     * 답글에서 2차 정렬
     *
     * @param parentSeq
     * @return
     */
    private String getReplyListOrder2(Long parentSeq) {
        BoardData data = boardDataRepository.findById(parentSeq).orElse(null);
        if (data == null) { // 처음 답글
            return "A1000";
        }

        int depth = data.getDepth() + 1; // 0 - 본글 - 답글,  1 답글 - 다답글, 2 다답글 - 다다답글

        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(boardData.parentSeq.eq(parentSeq))
                .and(boardData.depth.eq(depth));

        long count = boardDataRepository.count(builder);
        long seqNum = 1000 + count;

        return data.getListOrder2() + "A" + seqNum;
    }

}
