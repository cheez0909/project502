package org.choongang.board.controllers.comment;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.board.entites.CommentData;
import org.choongang.board.service.BoardAuthService;
import org.choongang.board.service.comment.CommentAuthService;
import org.choongang.board.service.comment.CommentInfoService;
import org.choongang.board.service.comment.CommentSaveService;
import org.choongang.commons.ExceptionRestProcessor;
import org.choongang.commons.rests.JSONData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class ApiCommentController implements ExceptionRestProcessor {

    private final CommentInfoService commentInfoService;
    private final CommentSaveService commentSaveService;
    private final CommentAuthService commentAuthService;
    private final BoardAuthService boardAuthService;

    /**
     * 레스트컨트롤러는 json으로 반환해야함
     * @param seq
     * @return
     */
    @GetMapping("/{seq}")
    private JSONData<CommentData> getComment(@PathVariable("seq") Long seq){
        CommentData data = commentInfoService.get(seq);

        return new JSONData<>(data);
    }


    @PatchMapping
    public JSONData<Object> editComment(RequestComment form){

        boardAuthService.check("comment_updata", form.getSeq());

        form.setMode("edit");
        commentSaveService.save(form);

        return new JSONData<>();
    }

    @GetMapping("/auth_check")
    public JSONData<Object> authCheck(@RequestParam("seq") Long seq){
        System.out.println("==========================");
        boardAuthService.check("comment_update", seq);
        return new JSONData<>();
    }

    @GetMapping("/auth_validate")
    public JSONData<Object> authValidate(@RequestParam("password") String password){
        boardAuthService.validate(password);
        return new JSONData<>();
    }
}
