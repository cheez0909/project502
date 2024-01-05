package org.choongang.commons.rests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JSONData<T> {
    // 상태코드
    private HttpStatus status = HttpStatus.OK;
    // 성공여부
    private boolean success = true;
    // body에 담을 데이터
    @NonNull
    private T data;
    // 메세지
    private String message;
}
