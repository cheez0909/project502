package org.choongang.email;

import org.choongang.email.service.EmailMessage;
import org.choongang.email.service.EmailSendService;
import org.choongang.email.service.EmailVerifyService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmailSendTest {

    @Autowired
    private EmailSendService emailSendService;
    @Autowired
    private EmailVerifyService emailVerifyService;

    @Test @Disabled
    public void emailSendTest1(){
        EmailMessage message = new EmailMessage("eun081217@gmail.com", "제목...", "내용...");
        boolean success = emailSendService.sendMail(message);
        // assertTrue(success);
    }

    @Test @Disabled
    public void sendWithTplTest(){
        EmailMessage message = new EmailMessage("bin0696@naver.com", "어우어려워,,,", "어렵다..,,");
        Map<String, Object> tplData = new HashMap<>();
        tplData.put("authNum", "123456");
        boolean success = emailSendService.sendMail(message, "auth", tplData);
    }

    @Test
    @DisplayName("이메일 인증 번호 전송 테스트")
    void emailVerifyTest() {
        boolean result = emailVerifyService.sendCode("bin0696@naver.com");
        assertTrue(result);
    }
}
