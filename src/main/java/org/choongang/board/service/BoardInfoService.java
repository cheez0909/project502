package org.choongang.board.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.BoardDataSearch;
import org.choongang.board.controllers.RequestBoard;
import org.choongang.board.entites.*;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.board.repositories.BoardViewRepository;
import org.choongang.board.service.config.BoardConfigInfoService;
import org.choongang.file.entites.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.member.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.choongang.commons.*;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardInfoService {

    private final EntityManager em;
    private final BoardDataRepository boardDataRepository;
    private final BoardConfigInfoService configInfoService;

    private final FileInfoService fileInfoService;
    private final HttpServletRequest request;

    private final Utils utils;
    private final MemberUtil memberUtil;
    private final BoardViewRepository boardViewRepository;


    /**
     * 게시글 조회
     *
     * @param seq : 게시글 번호
     * @return
     */
    public BoardData get(Long seq){
        BoardData boardData = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);
        addBoardData(boardData);

        return boardData;
    }

    /**
     * boardData -> RequestBoard형태로 바꿔줌
     * @param data : 게시글 데이터(BoardData), 게시글 번호(Long)
     * @return
     */
    public RequestBoard getForm(Object data){
        BoardData boardData = null;
        if(data instanceof BoardData){
            boardData = (BoardData) data;
        } else{
            Long seq = (Long) data;
            boardData = get(seq);
        }
        RequestBoard form = new ModelMapper().map(boardData, RequestBoard.class);
        form.setMode("update");
        form.setBid(boardData.getBoard().getBid());
        return form;
    }

    /**
     * 게시글 추가 정보 처리
     *
     * @param boardData
     */
    public void addBoardData(BoardData boardData){
        String gid = boardData.getGid();
        List<FileInfo> editorFiles = fileInfoService.getListDone(gid, "editor");
        List<FileInfo> attachFiles = fileInfoService.getListDone(gid, "attach");
        boardData.setEditorFiles(editorFiles);
        boardData.setAttachFiles(attachFiles);
    }

    /**
     * 특정 게시판 목록을 조회
     *  - 커맨드객체가 필요함(조회)
     * @param bid : 게시판 ID
     * @return
     */
    public ListData<BoardData> getList(String bid, BoardDataSearch search){

        Board board = configInfoService.get(bid);
        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), board.getRowsPerPage()); // 0일때 기본설정값, 0이 아니면 설정된 값
        int offset = (page -1) * limit; // 레코드 시작 위치

        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();

        andBuilder.and(boardData.board.bid.eq(bid)); // 게시판 ID

        /* 검색 조건 처리 S */
        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";
        if(StringUtils.hasText(skey)) {
            skey = skey.trim();

            BooleanExpression subjectCond = boardData.subject.contains(skey); // 제목 subject like '%skey%';
            BooleanExpression contentCond = boardData.content.contains(skey); // 내용 content like '%skey%';

            if (sopt.equals("SUBJECT")) { // 제목
                andBuilder.and(subjectCond);
            } else if (sopt.equals("CONTENT")) { // 내용
                andBuilder.and(contentCond);
            } else if (sopt.equals("SUBJECT_CONTENT")) { // 제목 + 내용
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(subjectCond)
                        .or(contentCond);
                andBuilder.and(orBuilder);
            } else if(sopt.equals("POSTER")) { // 작성자 + 아이디 + 회원명
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(boardData.poster.contains(skey))
                        .or(boardData.member.userId.contains(skey))
                        .or(boardData.member.name.contains(skey));
                andBuilder.and(orBuilder);
            }
        }

        // 특정 사용자로 게시글 한정 : 마이페이지에서 활용 가능
        String userId = search.getUserId();
        if(StringUtils.hasText(userId)){
            andBuilder.and(boardData.member.userId.eq(userId));
        }

        // 게시글 분류 조회
        String category = search.getCategory();
        if(StringUtils.hasText(category)){
            category = category.trim();
            andBuilder.and(boardData.category.eq(category));
        }
        /* 검색 조건 처리 E */

        PathBuilder<BoardData> pathBuilder = new PathBuilder<>(BoardData.class, "boardData"); // 정렬을 위해 추가

        List<BoardData> items = new JPAQueryFactory(em)
                .selectFrom(boardData)
                .leftJoin(boardData.member)
                .fetchJoin() // 지연로딩에서 n+1이 발생할 수 있음
                .offset(offset)
                .limit(limit)
                .where(andBuilder)
                .orderBy(
                        new OrderSpecifier(Order.DESC, pathBuilder.get("notice")) // 공지사항 먼저
                        ,new OrderSpecifier(Order.DESC, pathBuilder.get("createdAt")) // 작성일
                        )
                .fetch();

        // 게시글 전체 갯수
        long total = boardDataRepository.count(andBuilder);

        int ranges = utils.isMobile() ? board.getPageCountMobile() : board.getPageCountPc(); // 페이지 갯수

        Pagination pagination = new Pagination(page, (int) total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 게시글 조회수 업데이트
     * @param seq : 게시글 번호
     */
    public void updateViewCount(Long seq){

        BoardData data = boardDataRepository.findById(seq).orElse(null);
        if(data == null) return;

        try {
            int uid = memberUtil.isLogin() ? memberUtil.getMember().getSeq().intValue() : utils.guestUid();
            BoardView boardView = new BoardView(seq, uid);
            boardViewRepository.saveAndFlush(boardView);
        } catch (Exception e){
            e.printStackTrace();
        }

        // 조회수 카운팅 -> 게시글에 업데이트
        QBoardView bv = QBoardView.boardView;
        int viewCount = (int) boardViewRepository.count(bv.seq.eq(seq));

        data.setViewCount(viewCount);

        boardViewRepository.flush();

    }
}
