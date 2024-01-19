package org.choongang.board.controllers.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
public class RequestComment {

    private String mode = "add";

    private Long seq; // 댓글 등록 번호

    @NotBlank
    private String commenter; // 작성자

    private String guestPw; // 비회원 비밀번호

    @NotBlank
    private String content; // 댓글 내용

    private Long boardDataSeq; // 게시글 번호
}
