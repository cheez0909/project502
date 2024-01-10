package org.choongang.member.controllers;

import lombok.Data;

/**
 * 검색 데이터 -> 요청 데이터!
 * 검색 데이터를 담을 커맨드 객체가 필요함
 */
@Data
public class MemberSearch {
    private int page = 1;
    private int limit = 20;
}
