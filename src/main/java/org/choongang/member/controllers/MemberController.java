package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {
    private final Utils utils;

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin requestJoin){

        return utils.tpl("member/join");
    }

    @PostMapping("/join")
    public String joinPs(@Valid @RequestParam() RequestJoin requestJoin, Errors errors){

        if(errors.hasErrors()){
            return utils.tpl("member/join");
        }

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(){
        return utils.tpl("member/login");
    }

}
