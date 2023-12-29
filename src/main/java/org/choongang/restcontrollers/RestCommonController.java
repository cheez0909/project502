package org.choongang.restcontrollers;

import org.choongang.commons.exceptions.CommonException;
import org.choongang.commons.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("org.choongang.restcontrollers")
public class RestCommonController {

    // 위 패키지 경로 내의 에러는 이쪽으로 수집됨
    @ExceptionHandler(Exception.class)
    // 응답코드와 함께 상태를 담는 클래스
    public ResponseEntity<JSONData<Object>> errorHandler(Exception e){

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        if(e instanceof CommonException){
            CommonException commonException = (CommonException) e;
            status = commonException.getStatus();
        }

        JSONData<Object> data = new JSONData<>();
        data.setSuccess(false);
        data.setStatus(status);
        data.setMessage(e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(status).body(data);
    }
}
