package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.board.service.GuestPasswordCheckException;
import org.choongang.commons.exceptions.AlertBackException;
import org.choongang.commons.exceptions.AlertException;
import org.choongang.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * mvc -> ddd로 설계 변경
 * CommonController
 */
public interface ExceptionProcessor {

//    default String alertErrorHandler(){
//
//    }
    @ExceptionHandler(Exception.class)
    default String errorHandler(Exception e, HttpServletResponse response, HttpServletRequest request, Model model) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if(e instanceof CommonException){
            CommonException status1 = (CommonException) e;
            status =  status1.getStatus();
        }

        response.setStatus(status.value());

        e.printStackTrace();

        if(e instanceof AlertException){
            // 자바 스크립트로 응답할때 필요
            String script = String.format("alert('%s');", e.getMessage());
            if( e instanceof AlertBackException){
                script +="history.back();";
            }
            model.addAttribute("script", script);
            // 자바 스크립트 Alert 형태로 응답
            return "common/_execute_script";
        }

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
