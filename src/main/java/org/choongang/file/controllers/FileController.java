package org.choongang.file.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.exceptions.AlertBackException;
import org.choongang.commons.exceptions.CommonException;
import org.choongang.file.service.FileDeleteService;
import org.choongang.file.service.FileDownloadService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController implements ExceptionProcessor {
    private final FileDeleteService deleteService;
    private final FileDownloadService downloadService;

    /* 업로드는 자바스크립트로!
    @GetMapping("/upload")
    public String upload(){
        return "front/upload";
    }
    */

    /*
    @GetMapping("/delete/{seq}")
    public void delete(@PathVariable("seq") Long seq, HttpServletResponse response){

        deleteService.delete(seq);
    }
     */

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model){
        try {
            deleteService.delete(seq);
            /* callbackFileDelete가 함수이면 실행하라는 의미 -> 삭제가 되면 이 함수가 실행함 */
            String script = String.format("if (typeof parent.callbackFileDelete=='function') parent.callbackFileDelete(%d);", seq);
            model.addAttribute("script", script);
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertBackException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return "common/_execute_script";
    }

    /* 예시본
    @ResponseBody // 출력하는 부분을 바꿔주기 위해 void로 변경
    @RequestMapping("/download/{seq}")
    public void download(@PathVariable("seq") Long seq, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=test.txt");
        // 응답헤더에 Content-Disposition가 있으면 바디가 파일로 바뀜
        // 응답에더에 위에꺼가 없으면 바디만 출력됨
        PrintWriter out = response.getWriter();
        out.println("test1");
        out.println("test2");
    }
     */

    @ResponseBody
    @RequestMapping("/download/{seq}")
    public void download(@PathVariable("seq") Long seq)  {
        try {
            downloadService.download(seq);
        } catch (CommonException e){
            throw new AlertBackException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
