package org.choongang.commons.exceptions;

import org.springframework.http.HttpStatus;

public class AlertException extends CommonException{
    public AlertException(String msg, HttpStatus status){
        super(msg, status);
    }
}
