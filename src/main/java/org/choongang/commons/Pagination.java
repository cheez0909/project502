package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Pagination {

    private int page;
    private int total;
    private int ranges;
    private int limit;

    private int preViewRangePage; // 이전 구간 첫 페이지 번호
    private int nextViewRangePage; // 다음 구간 첫 페이지 번호

    private int firstRangePage; // 구간의 시작 페이지 번호
    private int lastRangePage; // 구간의 마지막 페이지 번호

    private int totalPages; // 전체 페이지 개수
    private String baseURL; // 페이징 쿼리스트링 기본URL

    /**
     * 페이지를 만들기 위해서 4가지가 필수임
     * @param page : 현재 페이지
     * @param total : 전체 레코드 갯수
     * @param ranges : 페이지 구간 갯수
     * @param limit : 1페이지 당 레코드 갯수
     * @param request : 검색을 위해서 요청객체가 필요함 (요청쪽 데이터)
     */
    public Pagination(int page, int total, int ranges, int limit, HttpServletRequest request){

        page = Utils.onlyPositiveNumber(page, 1);
        total = Utils.onlyPositiveNumber(total, 0);
        ranges = Utils.onlyPositiveNumber(ranges, 10); // 페이지 블럭갯수
        limit = Utils.onlyPositiveNumber(limit, 20); // 1페이지당 레코드 갯수

        // 전체 페이지 갯수 : Math.ceil -> 올림함수 / 정수/정수 = 정수 이므로 더블형으로 변환
        totalPages = (int) Math.ceil( total / (double)limit);

        // 구간 번호
        int rangeCnt = (page - 1) / ranges; // 현재 페이지 구간 -> 첫번째 구간(ex.1~5)인지 두번째 구간(ex.6~10)인지
        firstRangePage = rangeCnt * ranges + 1; // 현재 구간의 시작 페이지 번호
        lastRangePage = firstRangePage + ranges -1; // 현재 구간의 마지막 페이지 번호

        lastRangePage = lastRangePage > totalPages ? totalPages : lastRangePage;

        // 이전 구간 첫 페이지
        if (rangeCnt > 0){
            preViewRangePage = firstRangePage - ranges;
        }

        // 다음 구간 첫 페이지
        int lastRangeCnt = (totalPages -1) / ranges;
        // 마지막 구간이 아닌 경우 다음 구간 첫 페이지
        if(rangeCnt < lastRangeCnt){
                nextViewRangePage = firstRangePage + ranges;
        }
        /**
         * 쿼리스트링 값 유지 처리 - 쿼리스트링 값 중에서 page만 제외하고 다시 조함
         * 예)  ?order=CASH&name=...&page=2 -> ?order=CASH&name=...&page=3..
         * ?page=2 -> ?
         * 없는 경우 -> ?
         * "&"로 split 하면
         * {"order=CASH", "name=...", "page=3"} -> 이런식으로 생김
         * => page를 제외한 나머지를 유지시킴
         */
        String baseURL = "?";

        if(request != null){
            String queryString = request.getQueryString();
            if(StringUtils.hasText(queryString)){
                queryString = queryString.replace("?", "");

                baseURL += Arrays.stream(queryString.split("&"))
                        .filter(s-> !s.contains("page="))
                        .collect(Collectors.joining("&"));
                baseURL = baseURL.length() > 1 ? baseURL+="&" : baseURL;
            }
        }

        this.page = page;
        this.total = total;
        this.ranges = ranges;
        this.limit = limit;
        this.firstRangePage =firstRangePage;
        this.lastRangePage = lastRangePage;
        this.baseURL = baseURL;

    }

    public Pagination(int page, int total, int ranges, int limit){
        this(page, total, ranges, limit, null);
    }

    public List<String[]> getPages(){
        // 0: 페이지 번호, 1: 페이지 URL - ?page=페이지번호
        // 템플릿에 출력할 데티어

        List<String[]> data = IntStream.rangeClosed(firstRangePage, lastRangePage)
                .mapToObj(p -> new String[] {String.valueOf(p), baseURL+"page="+p}).toList();

        // 1페이지 일때 ?page=1 , 2페이지 일때, ?page=2..

        return data;
    }
}
