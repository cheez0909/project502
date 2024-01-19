package org.choongang.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.choongang.board.controllers.comment.RequestComment;
import org.choongang.board.entites.Board;
import org.choongang.board.entites.BoardData;
import org.choongang.board.repositories.BoardViewRepository;
import org.choongang.board.service.*;
import org.choongang.board.service.config.BoardConfigInfoService;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.ListData;
import org.choongang.commons.Utils;
import org.choongang.file.entites.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@SessionAttributes("seq")
public class BoardController implements ExceptionProcessor {

    /*
    private final BoardDataRepository boardDataRepository;

    @GetMapping("/test")
    @ResponseBody
    private void test(){
        BoardData referenceById = boardDataRepository.getReferenceById(1L);
        referenceById.setSubject("(수정)");
        boardDataRepository.saveAndFlush(referenceById);
    }

    @GetMapping("/test")
    @ResponseBody
    public void test(){
        BoardData data = new BoardData();
        data.setContent("내용");
        data.setSubject("제목");
        boardDataRepository.saveAndFlush(data);
    }


    @GetMapping("/test2")
    // @PreAuthorize("isAuthenticated()")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public void test2(){
        System.out.println("test2!!!");
    }
    */


    private final Utils utils;
    private final BoardConfigInfoService configInfoService;
    private final MemberUtil memberUtil;
    private final FileInfoService fileInfoService; // 파일 정보조회를 위해(미완료된 파일도 전체 조회가 필요함)
    private final BoardFormValidator boardFormValidator;
    private final BoardSaveService boardSaveService;
    private final BoardInfoService boardInfoService;
    private final BoardDeleteService boardDeleteService;
    private final BoardViewRepository boardViewRepository;
    private final BoardAuthService boardAuthService;


    private Board board;
    private BoardData boardData;

    /**
     * @param bid   : 게시판 아이디
     * @param model
     * @return
     */
    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid, @ModelAttribute BoardDataSearch search, Model model) {
        commonProcess(bid, "list", model);

        ListData<BoardData> data = boardInfoService.getList(bid, search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());
        return utils.tpl("board/list");
    }

    /**
     * 게시글 보기
     *
     * @param seq   : 게시글 번호
     * @param model
     * @return
     */
    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, @ModelAttribute BoardDataSearch search, Model model) {
        boardInfoService.updateViewCount(seq);

        commonProcess(seq, "view", model);

        // 게시글 보기 하단 목록 노출 S
        if(board.isShowListBelowView()){ // 게시글 보기 하단 목록 노출
            ListData<BoardData> data = boardInfoService.getList(board.getBid(), search);

            model.addAttribute("items", data.getItems());
            model.addAttribute("pagination", data.getPagination());
        }
        // 게시글 보기 하단 목록 노출 E

        // 댓글 커맨드 객체 처리 S
        RequestComment requestComment = new RequestComment();
        if(memberUtil.isLogin()){
            requestComment.setCommenter(memberUtil.getMember().getName());
        }
        model.addAttribute("requestComment", requestComment);
        // 댓글 커맨드 객체 처리 E

        return utils.tpl("board/view");
    }

    /**
     * 게시글 작성
     *
     * @param bid   : 게시글 번호
     * @param model
     * @return
     */
    @GetMapping("/write/{bid}")
    public String wirte(@PathVariable("bid") String bid,
                        @ModelAttribute RequestBoard form,
                        Model model) {
        commonProcess(bid, "write", model);
        if(memberUtil.isLogin()) {
            Member member = memberUtil.getMember();
            form.setPoster(member.getName());
        }
        return utils.tpl("board/write");
    }

    /**
     * 게시글 수정
     *
     * @param seq   : 시퀀스 번호
     * @param model
     * @return
     */
    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "update", model);
        RequestBoard form = boardInfoService.getForm(boardData);
        model.addAttribute("requestBoard", form);
        return utils.tpl("board/update");
    }

    /**
     * 게시글 등록, 수정 -> 완료 되면 VIEW로 갈 지 리스트로 갈 지 결정
     *
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestBoard form, Errors errors, Model model) {
        String bid = form.getBid();
        String mode = form.getMode();

        commonProcess(bid, mode, model);

        boardFormValidator.validate(form, errors);

        // 오류가 났을 때 전체 파일을 조회할 수 있도록
        if(errors.hasErrors()){
            String gid = form.getGid();

            List<FileInfo> editorFiles = fileInfoService.getList(gid, "editor");
            List<FileInfo> attachFiles = fileInfoService.getList(gid, "attach");

            form.setEditorFiles(editorFiles);
            form.setAttachFiles(attachFiles);

            return utils.tpl("board/" + mode);
        }

        BoardData boardData = boardSaveService.save(form);

        // Long seq = 0L; // 임시 나중에 제거

        String redirectURL = "redirect:/board/";
        // 글 작성 후 이동 장소 : 블로그는 작성 후 리스트로....
        redirectURL += board.getLocationAfterWriting().equals("view") ? "view/" + boardData.getSeq() : "list/" + form.getBid();

        return redirectURL;
    }

    // 게시판 아이디를 가지고 게시판 모드를 유지함

    /**
     * 게시판의 공통 처기 부분 : 글목록, 글쓰기 등 게시판 아이디가 필요한 경우
     *
     * @param bid   : 게시판 아이디
     * @param mode  : 블로그 형식인지, 사진형식인지 등등... 스킨 설정
     * @param model
     */
    private void commonProcess(String bid, String mode, Model model) {

        mode = StringUtils.hasText(mode) ? mode : "list";

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        List<String> addCss = new ArrayList<>();
        List<String> addCommonCss = new ArrayList<>();

        /* 게시판 설정 처리 S */
        board = configInfoService.get(bid); // 새로 조회

        // 접근 권한 체크
        boardAuthService.accessCheck(mode, board);

        // 스킨별 css, js 추가 : 한 파일에 모아서 하면 스타일이 중복될 수 있음
        String skin = board.getSkin();
        addCss.add("board/skin_" + skin);
        addScript.add("board/skin_" + skin);

        model.addAttribute("board", board);
        /* 게시판 설정 처리 E */

        String pageTitle = board.getBName(); // 게시판명이 기본 타이틀

        if (mode.equals("write") || mode.equals("update")) { // 쓰기 또는 수정
            if (board.isUseEditor()) { // 에디터 사용하는 경우
                addCommonScript.add("ckeditor5/ckeditor");
            }

            // 이미지 또는 파일 첨부를 사용하는 경우
            if (board.isUseUploadImage() || board.isUseUploadFile()) {
                addCommonScript.add("fileManager");
            }

            addScript.add("board/form");

            pageTitle += " ";
            pageTitle += mode.equals("update") ? Utils.getMessage("글수정", "commons") : Utils.getMessage("글쓰기", "commons");


        } else if(mode.equals("view")){
            // pageTitle - 글제목 - 게시판명
            String subject = boardData.getSubject();
            pageTitle = String.format("%s | %s", subject, board.getBName());
            addScript.add("board/view");
        }

        model.addAttribute("addCommonCss", addCommonCss);
        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("pageTitle", pageTitle);
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model){
        commonProcess(seq, "delete", model);

        boardDeleteService.delete(seq);

        return "redirect:/board/list/" + board.getBid();
    }

    /**
     * 비회원 글 수정, 삭제
     * @param password
     * @param model
     * @return
     */

    @PostMapping("/password")
    public String passwordCheck(@RequestParam(name="password", required = false) String password, Model model) {
        boardAuthService.validate(password);

        model.addAttribute("script", "parent.location.reload();");

        return "common/_execute_script";
    }
    /**
     * 게시판 공통 처리 : 게시글 보기, 수정 -> 게시글 번ㅇ호가 있는 경우
     *  - 게시글 조회 후 -> 게시글 설정을 가져옴
     * @param seq : 게시판 번호
     * @param mode
     * @param model
     */
    private void commonProcess (Long seq, String mode, Model model){
        model.addAttribute("seq", seq);
        model.addAttribute("mode", mode);
        boardAuthService.check(mode, seq);

        /* 게시판 설정 처리 S */
        boardData = boardInfoService.get(seq);
        String bid = boardData.getBoard().getBid();

        commonProcess(bid, mode, model);
        model.addAttribute("boardData", boardData);
        /* 게시판 설정 처리 E */
    }


    @Override
    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception e, HttpServletResponse response, HttpServletRequest request, Model model) {
        if(e instanceof GuestPasswordCheckException){
            return utils.tpl("board/password");
        }
        return ExceptionProcessor.super.errorHandler(e, response, request, model);
    }
}