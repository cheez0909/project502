package org.choongang.admin.controllers;

import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminMainController")
@RequestMapping("/admin")
public class MainController implements ExceptionProcessor {
    /**
     * 관리자 첫 페이지
     */
    @GetMapping
    public String index(){
        return "admin/main/index";
    }


}
