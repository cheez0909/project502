package org.choongang.board.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entites.BoardData;
import org.choongang.board.repositories.BoardDataRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardDataRepository boardDataRepository;

    @GetMapping("/test")
    @ResponseBody
    private void test(){
        BoardData referenceById = boardDataRepository.getReferenceById(1L);
        referenceById.setSubject("(수정)");
        boardDataRepository.saveAndFlush(referenceById);
    }
    /*
    @GetMapping("/test")
    @ResponseBody
    public void test(){
        BoardData data = new BoardData();
        data.setContent("내용");
        data.setSubject("제목");
        boardDataRepository.saveAndFlush(data);
    }
     */

    @GetMapping("/test2")
    // @PreAuthorize("isAuthenticated()")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public void test2(){
        System.out.println("test2!!!");
    }
}
