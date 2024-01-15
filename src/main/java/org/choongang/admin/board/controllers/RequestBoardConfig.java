package org.choongang.admin.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

/**
 * 게시판 커맨드 객체
 */
@Data
public class RequestBoardConfig {

    private String mode = "add";
    private String gid = UUID.randomUUID().toString();

    @NotBlank
    private String bid; // 게시판 아이디

    @NotBlank
    private String bName; // 게시판 이름

    private boolean active; // 사용 여부

    private int rowsPerPage = 20; // 1페이지 게시물 수

    private int pageCountPc = 10; // pc페이지 구간 갯수
    private int pageCountMobile = 5; // mobile 페이지 구간 갯수

    private boolean useComment; // 댓글 사용 여부
    private boolean useEditor; // 에디터 사용 여부

    private boolean useUploadImage; // 이미지 첨부 사용 여부
    private boolean useUploadFile; // 이미지 첨부 사용 여부

    private String locationAfterWriting = "list"; // 글 작성 후 이동 리스트는 목록 뷰는 글

    private String skin="default"; // 스킨

    private String category; // 게시판 분류

    private String viewAccessType="ALL"; // 권한 설정 - 글보기
    private String listAccessType="ALL"; // 권한 설정 - 글목록
    private String writeAccessType = "ALL"; // 권한 설정 - 글쓰기
    private String replyAccessType = "ALL"; // 권한 설정 - 답글
    private String commentAccessType = "ALL"; // 권한 설정 - 댓글

    private String htmlTop; // 게시판 상단 html
    private String htmlBottom; // 게시판 하단 html

    @Transient
    private List<FileInfo> htmlTopImages; // 게시판 상단 Top 이미지

    @Transient
    private List<FileInfo> htmlBottomImages; // 게시판 하단 Bottom 이미지
}
