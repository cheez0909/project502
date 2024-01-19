package org.choongang.board.controllers.comment;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.validators.PasswordValidator;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CommentFormValidator implements Validator, PasswordValidator {

    // 로그인 여부 체크
    private final MemberUtil memberUtil;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestComment.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        // 로그인 상태 일때는 비회원 비밀번호 체크 X
        if (memberUtil.isLogin()){
            return;
        }

        RequestComment form = (RequestComment) target;

        String guestPw = form.getGuestPw();

        if(!StringUtils.hasText(guestPw)){
            errors.rejectValue("guestPw", "NotBlank");
        }

        // 명확하지 않을때는 validator를 이용하는 것이 좋다
        // @Size -> 는 명확할 때
        if(StringUtils.hasText(guestPw)){
            if(guestPw.length() < 6) {
                errors.rejectValue("guestPw", "Size");
            }

            if(!alphaCheck(guestPw, true)||!numberCheck(guestPw)){
                errors.rejectValue("guestPw", "Complexity");
            }
        } // endif
    }
}
