package org.choongang.commons;

import org.choongang.commons.exceptions.CommonException;
import org.choongang.commons.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * mvc -> ddd로 설계 변경
 * RestCommonController
 */
public interface ExceptionRestProcessor {


    @ExceptionHandler(Exception.class)
    // 응답코드와 함께 상태를 담는 클래스
    default ResponseEntity<JSONData<Object>> errorHandler(Exception e){

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
