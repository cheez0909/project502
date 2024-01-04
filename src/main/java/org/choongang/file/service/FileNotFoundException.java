package org.choongang.file.service;

import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class FileNotFoundException extends CommonException {
    public FileNotFoundException(){
        super(Utils.getMessage("NotFound.file", "errors"), HttpStatus.NOT_FOUND);
    }
}
