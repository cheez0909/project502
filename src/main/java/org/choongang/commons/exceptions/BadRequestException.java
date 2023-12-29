package org.choongang.commons.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CommonException{
    public BadRequestException(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }
}
