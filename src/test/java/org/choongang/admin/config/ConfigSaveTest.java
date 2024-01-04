package org.choongang.admin.config;

import com.fasterxml.jackson.core.type.TypeReference;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.admin.config.service.ConfigInfoService;
import org.choongang.admin.config.service.ConfigSaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
public class ConfigSaveTest {
    @Autowired
    private ConfigSaveService configSaveService;
    @Autowired
    private ConfigInfoService infoService;

    @Test
    void saveTest(){
        BasicConfig basicConfig = new BasicConfig();
        basicConfig.setSiteTitle("사이트 제목");
        basicConfig.setJoinTerms("사이트 설명");
        basicConfig.setSiteKeywords("사이트 키워드");
        basicConfig.setCssJsVersion(1);
        basicConfig.setJoinTerms("회원가입 약관");
        configSaveService.save("basic", basicConfig);

        // 배열의 형태로 나옴
        BasicConfig basicConfig1 = infoService.get("basic", BasicConfig.class).orElse(null);
        System.out.println(basicConfig1);

        //   제이슨은 키와 값임
        //  이것을 맵으로 바꿔서 보고 싶을 때
        // 맵이나 리스트로 사용할 땐 타입 레퍼런스로 사용해야함.
        Map<String, String> config3 = infoService.get("basic", new TypeReference<Map<String, String>>() {
        }).orElse(null);
        System.out.println(config3);
    }
}
