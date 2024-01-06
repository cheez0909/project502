package org.choongang.file.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.file.service.FileDeleteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.http.HttpResponse;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController implements ExceptionProcessor {
    private final FileDeleteService deleteService;
    @GetMapping("/upload")
    public String upload(){
        return "front/upload";
    }

    @GetMapping("/delete/{seq}")
    public void delete(@PathVariable("seq") Long seq, HttpServletResponse response){

        deleteService.delete(seq);
    }
}
