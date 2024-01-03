package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * mvc -> ddd로 설계 변경
 * CommonController
 */
public interface ExceptionProcessor {
    @ExceptionHandler(Exception.class)
    default String errorHandler(Exception e, Model model, HttpServletResponse response, HttpServletRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if(e instanceof CommonException){
            CommonException status1 = (CommonException) e;
            status =  status1.getStatus();
        }

        response.setStatus(status.value());

        e.printStackTrace();
        /**
         * 상태코드, 경로, 메세지, 메서드(요청방식)
         */
        model.addAttribute("status", status.value());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("method", request.getMethod());
        model.addAttribute("meesage", e.getMessage());
        return "error/common";
    }
}
