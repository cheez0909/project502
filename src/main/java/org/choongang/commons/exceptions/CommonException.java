package org.choongang.commons.exceptions;

import org.springframework.http.HttpStatus;

public class CommonException extends RuntimeException{
    private HttpStatus status; // 상태코드

    public CommonException(String msg, HttpStatus status){
        super(msg);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return this.status;
    }

}
