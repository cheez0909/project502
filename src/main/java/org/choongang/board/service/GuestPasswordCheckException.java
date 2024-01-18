package org.choongang.board.service;

import org.choongang.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class GuestPasswordCheckException extends CommonException {
    public GuestPasswordCheckException() {
        super(HttpStatus.UNAUTHORIZED);
    }
}
