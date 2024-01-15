package org.choongang.board.service;

import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class BoardDataNotFoundException extends AlertBackException {
    public BoardDataNotFoundException() {
        super(Utils.getMessage("NotFound.BoardData", "errors"), HttpStatus.NOT_FOUND);
    }
}
