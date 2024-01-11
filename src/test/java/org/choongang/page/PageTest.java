package org.choongang.page;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.commons.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class PageTest {

    @Mock
    // 인터페이스 모의 객체(가짜 서버)
    // 톰캣서버가 없을 때 만듦
    private HttpServletRequest request;


    @Test
    @DisplayName("페이지 구간별 데이터 테스트")
    void pageTest1(){
        Pagination pagination = new Pagination(1, 240, 5, 20);
        List<String[]> pages = pagination.getPages();
        pages.forEach(s-> System.out.println(Arrays.toString(s)));
        int totalPages = (int)Math.ceil(pagination.getTotal() / (double) pagination.getLimit());
    }

    @Test
    @DisplayName("페이징 쿼리스트링 유지되는지 테스트")
    void pageTest2(){
        given(request.getQueryString()) // 쿼리스트링 만들기
                .willReturn("?orderStatus=CASH&userName=name&page=3");

        Pagination pagination = new Pagination(23, 1234, 5, 20, request);
        List<String[]> pages = pagination.getPages();
        pages.forEach(s-> System.out.println(Arrays.toString(s)));
        int totalPages = (int)Math.ceil(pagination.getTotal() / (double) pagination.getLimit());
    }
}
