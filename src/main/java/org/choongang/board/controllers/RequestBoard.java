package org.choongang.board.controllers;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.choongang.file.entites.FileInfo;

import java.util.*;

@Data
public class RequestBoard {
    private String mode = "write";
    private Long seq;
    private String bid;
    private String gid = UUID.randomUUID().toString();
    private String category; // 게시판 분류

    @NotBlank
    private String poster; // 글 작성자
    private String guestPw; // 비회원 비밀번호 비회원일때만 보여야됨
    private boolean notice; // 공지사항 여부 (관리자일때 체크)
    
    @NotBlank
    private String subject; // 글 제목
    @NotBlank
    private String content; // 글 내용
    
    private Long parentSeq; // 부모 게시글 번호 - 답글

    private Long num1; // 추가 필드 - 정수
    private Long num2; // 추가 필드 - 정수
    private Long num3; // 추가 필드 - 정수

    private String text1; // 추가 필드 - 한줄 텍스트
    private String text2; // 추가 필드 - 한줄 텍스트
    private String text3; // 추가 필드 - 한줄 텍스트


    private String longText1; // 추가 필드 : 여러줄 텍스트
    private String longText2; // 추가 필드 : 여러줄 텍스트
    private String longText3; // 추가 필드 : 여러줄 텍스트

    // 미완료되었을 때 파일 유지를 위해서 추가
    private List<FileInfo> editorFiles; // 에디터 파일 목록
    private List<FileInfo> attachFiles; // 첨부 파일 목록
}
