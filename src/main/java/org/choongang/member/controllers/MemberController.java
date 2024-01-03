package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.choongang.member.services.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {

    private final Utils utils;
    private final JoinService joinService;

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin requestJoin){

        return utils.tpl("member/join");
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors){
        joinService.process(form, errors);
        if(errors.hasErrors()){
            return utils.tpl("member/join");
        }
        return "redirect:/member/login";
    }
    @GetMapping("/login")
    public String login(){
        return utils.tpl("member/login");
    }

    /**
     *
     * @param principal : getName만 존재함 -> 아이디만 조회할 수 있음
     * @return
     */
    /*
    @GetMapping("/info")
    @ResponseBody
    public String info(Principal principal){
        return principal.getName(); // 아이디만 조회할 수 있음!
    }
    */

    /**
     * @AuthenticationPrincipa -> UserDetails의 구현체를 가져옴
     */
    /*
    @GetMapping("/info")
    @ResponseBody
    public String info(@AuthenticationPrincipal MemberInfo memberInfo){
        return memberInfo.toString();
    }
     */

    /*
    @GetMapping("/info")
    @ResponseBody
    public String info(){
        MemberInfo memberInfo =(MemberInfo) SecurityContextHolder.getContext()
                .getAuthentication() // 로그인 정보가 담겨있는 객체 Authentication -> 로그인 성공시 회원정보를 담는 객체
                .getPrincipal();

        // 편의상 구하려면 로그인 성공시 세션에 Authentication객체를 담아서 활용할 수 있다.
        return memberInfo.toString();
    }
     */

    /*
    @GetMapping("/info")
    @ResponseBody
    public String info(){
    }
     */
}
