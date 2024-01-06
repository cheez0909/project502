package org.choongang.commons.exceptions;

import org.springframework.http.HttpStatus;

public class AlertBackException extends AlertException{
    public AlertBackException(String msg, HttpStatus status){
        super(msg, status);
    }
}
