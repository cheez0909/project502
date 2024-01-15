package org.choongang.admin.board.controllers;

@Data
public class BoardSearch extends MemberSearch{
    private String bid;
    private String bName;
    private boolean active;

    private String sopt; // 검색 옵션
    private String skey; // 검색 키워드
}
