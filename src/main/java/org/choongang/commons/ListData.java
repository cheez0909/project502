package org.choongang.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 목록 데이터는 다양할 수 있음 -> 멤버, 보드 등등...
 * 페이징과 목록을 함께 가져오는 객체를 지네릭 만듦
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListData<T> {
    private List<T> items;
    private Pagination pagination;
}
