package org.choongang.admin.board.controllers;

import jakarta.validation.Valid;
import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.commons.ExceptionProcessor;
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

@Controller("adminBoardController")
@RequestMapping("/admin/board")
public class BoardController implements ExceptionProcessor {
    private final BoardConfigSaveService configSaveService;
    private final BoardConfigInfoService configInfoService;

    private final BoardConfigValidator configValidator;
    @ModelAttribute("menuCode")
    public String getMenuCode(){ // 주 메뉴코드
        return "board";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus(){ // 서브 메뉴
        return Menu.getMenus("board");
    }

    /**
     * 게시판 목록
     */
    @GetMapping
    public String list(@ModelAttribute BoardSearch search, Model model) {
        commonProcess("list", model);

        ListData<Board> data = configInfoService.getList(search);

        List<Board> items = data.getItems();
        Pagination pagination = data.getPagination();

        model.addAttribute("items", items);
        model.addAttribute("pagination", pagination);

        return "admin/board/list";
    }

    /**
     * 게시판 등록
     *
     * @return
     */
    @GetMapping("/add")
    public String add(@ModelAttribute RequestBoardConfig config, Model model){
        commonProcess("add", model);
        return "admin/board/add";
    }

    @GetMapping("/edit/{bid}")
    public String edit(@PathVariable("bid") String bid, Model model) {
        commonProcess("edit", model);

        RequestBoardConfig form = configInfoService.getForm(bid);
        System.out.println(form);
        model.addAttribute("requestBoardConfig", form);

        return "admin/board/edit";
    }
    /**
     * 게시판 등록/수정 처리
     * 추가와 수정을 동시에
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestBoardConfig config, Errors errors, Model model){
        String mode = config.getMode();

        commonProcess(mode, model);

        if(errors.hasErrors()){
            return "admin/board/" + mode;
        }
        // 저장일수도 있고 목록일수도 있음
        return "redirect:/admin/board";
    }

    /**
     * 게시글 관리
     *
     * @return
     */
    @GetMapping("/posts")
    public String posts(Model model){
        commonProcess("posts", model);
        return "admin/board/posts";
    }


    /**
     * 공통처리
     * @param mode
     * @param model
     */
    private static void commonProcess(String mode, Model model){
        String pageTitle = "게시판 목록";
        mode = StringUtils.hasText(mode) ? mode : "list";



        if(mode.equals("add")){
            pageTitle = "게시판 등록";
        } else if(mode.equals("edit")){
            pageTitle = "게시판 수정";
        } else if(mode.equals("posts")){
            pageTitle = "게시글 관리";
        }



        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        if(mode.equals("add") || mode.equals("edit")){
            //게시판 등록 또는 수정일 때 스크립트를 추가
            addCommonScript.add("ckeditor5/ckeditor");
            addCommonScript.add("fileManager");
            addScript.add("board/form");
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subMenuCode", mode);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
    }
}
