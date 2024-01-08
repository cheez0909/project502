package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.choongang.member.services.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {

    private final Utils utils;
    private final JoinService joinService;

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin requestJoin, Model model){
        commonProcess("join", model);
        // model.addAttribute("pageTitle", "회원가입");
        return utils.tpl("member/join");
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model){
        commonProcess("join", model);
        joinService.process(form, errors);
        if(errors.hasErrors()){
            return utils.tpl("member/join");
        }
        return "redirect:/member/login";
    }
    @GetMapping("/login")
    public String login(Model model){
        commonProcess("login", model);
        return utils.tpl("member/login");
    }

    /**
     * pageTitle 공통 처리 하기!!
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model){
        mode = StringUtils.hasText(mode) ? mode : "join";
        String pageTitle = Utils.getMessage("회원가입", "commons");

        List<String> addCommonScript = new ArrayList<>(); // 공통 자바스크립트
        List<String> addScript = new ArrayList<>(); // 프론트 자바스크립트

        if(mode.equals("login")){
            pageTitle = Utils.getMessage("로그인", "commons");
        } else if(mode.equals("join")){
            addCommonScript.add("fileManager");
            addScript.add("member/form");
        }
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        // 프론트에만 필요하면 프론트로
        // 파일기능은 공통이기 때문에 common에 넣음
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
