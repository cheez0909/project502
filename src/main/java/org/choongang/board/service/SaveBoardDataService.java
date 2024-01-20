package org.choongang.board.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.BoardDataSearch;
import org.choongang.board.entites.SaveBoardData;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.board.repositories.SaveBoardDataRepository;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.choongang.board.entites.*;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class SaveBoardDataService {

    private final MemberUtil memberUtil;
    private final BoardInfoService boardInfoService;
    private final SaveBoardDataRepository saveBoardDataRepository;
    private final BoardDataRepository boardDataRepository;
    private final HttpServletRequest request;

    /**
     * 게시글 번호 찜하기
     *
     * @param bSeq
     */
    public void save(Long bSeq){
        if(!memberUtil.isLogin()){
            return;
        }

        try {
            SaveBoardData data = new SaveBoardData();
            data.setBSeq(bSeq);
            data.setMSeq(memberUtil.getMember().getSeq());
            saveBoardDataRepository.saveAndFlush(data);
        } catch (Exception e) {

        }
    }


    /**
     * 게시글 번호 찜하기
     *
     * @param bSeq
     */
    public void delete(Long bSeq){
        if(!memberUtil.isLogin()){
            return;
        }

        SaveBoardDataId id = new SaveBoardDataId(bSeq, memberUtil.getMember().getSeq());

        SaveBoardData data = saveBoardDataRepository.findById(id).orElse(null);

        if(data != null){ // 찜한 기록이 있는 경우만 삭제 진행
            saveBoardDataRepository.delete(data);
            saveBoardDataRepository.flush();
        }
    }

    /**
     * 찜한 모든 데이터 삭제
     */
    public void deleteAll(){
        if(!memberUtil.isLogin()){
            return;
        }
        QSaveBoardData saveBoardData = QSaveBoardData.saveBoardData;
        List<SaveBoardData> items = (List<SaveBoardData>) saveBoardDataRepository.findAll(saveBoardData.mSeq.eq(memberUtil.getMember().getSeq()));

        saveBoardDataRepository.deleteAll(items);
        saveBoardDataRepository.flush();
    }

    /**
     * 찜한 게시글 인지 체크
     * @return
     */
    public boolean saved(Long bSeq){
        if(memberUtil.isLogin()){
            SaveBoardDataId id = new SaveBoardDataId(bSeq, memberUtil.getMember().getSeq());

//            QSaveBoardData saveBoardData = QSaveBoardData.saveBoardData;
//            BooleanBuilder builder = new BooleanBuilder();
//            Long mSeq = memberUtil.getMember().getSeq();

            return saveBoardDataRepository.existsById(id);
        }
        return false;
    }

    /**
     * 찜한 게시글 목록
     *
     * @param search
     * @return
     */
    public ListData<BoardData> getList(BoardDataSearch search){

        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 20);

        Member member = memberUtil.getMember();
        Long mSeq = member.getSeq(); // 임시로 멤버쪽에 있는 것을 가져옴

        // 임시로 고정
        mSeq =1L;

        List<Long> bSeqs = saveBoardDataRepository.getBoardDataSeqs(mSeq);

        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(boardData.seq.in(bSeqs)); // 찜한 게시글 가져와서

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

        // 분류 검색
        String category = search.getCategory();
        if(StringUtils.hasText(category))
        {
            andBuilder.and(boardData.category.eq(category.trim()));
        }
        /* 검색 조건 처리 E */



        Pageable pageable = PageRequest.of(page-1, limit);
        Page<BoardData> data = boardDataRepository.findAll(andBuilder, pageable);
        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);
        List<BoardData> items = data.getContent();
        items.forEach(boardInfoService::addBoardData); // 추가 데이터 처리 썸네일, 이미지 등등

        return  new ListData<>(items, pagination);
    }
}