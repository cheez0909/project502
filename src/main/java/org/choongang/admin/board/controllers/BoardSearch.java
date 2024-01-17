package org.choongang.admin.board.controllers;

import lombok.Data;
import org.choongang.member.controllers.MemberSearch;

import java.util.List;

@Data
public class BoardSearch extends MemberSearch {
    private String bid;
    private List<String> bids;
    private String bName;
    private boolean active;

    private String sopt; // 검색 옵션
    private String skey; // 검색 키워드
}
