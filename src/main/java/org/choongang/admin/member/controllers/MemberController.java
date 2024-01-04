package org.choongang.admin.member.controllers;


import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("adminMemberController")
@RequestMapping("/admin/member")
public class MemberController implements ExceptionProcessor {
    // 메뉴는 공통으로 쓰는 부분임

    /**
     * subMenus 라는 속성값이 있으면 값이 모든 컨트롤러에서 공유가 됨
     */
    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus(){
        return Menu.getMenus("member");
    }
    @ModelAttribute("menuCode")
    public String getMenuCode(){
        return "member";
    }

    @GetMapping
    public String list(Model model){
        // 컨트롤러어드바이스를 이용하면 됨
        model.addAttribute("subMenuCode", "list");
        return "admin/member/list";
    }
}
