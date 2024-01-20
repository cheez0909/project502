package org.choongang.board.controllers;

import lombok.Data;
import java.util.*;

@Data
public class BoardDataSearch {
    private int page = 1;
//    private int limit = 20; 설정이 있기 때문에 설정에 따름 -> 1페이지 게시글 수
    private int limit; // 0:설정에 있는 1페이지 게시글 갯수, 1이상이면 지정한 갯수
    /**
     * 검색 옵션
     *
     * subject : 제목
     * content : 내용
     * subject_content : 제목_내용(OR)
     * poster : 작성자명 + 아이디 + 회원이름(OR)
     * ALL : 전체
     */
    private String sopt; // 검색 옵션
    private String skey; // 검색 조건

    private List<String> bid; // 게시판 ID : 전체 게시판조회 or 특정게시판을 조회할 수 있음

    private String userId; // 특정 아이디를 고정해서 검색 -> 마이페이지 작성할 때
    private String category; // 게시판 분류

}
