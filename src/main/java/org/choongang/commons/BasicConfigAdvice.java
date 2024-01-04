package org.choongang.commons;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.admin.config.service.ConfigInfoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(value = "org.choongang", name="siteConf") // 범위 설정
@RequiredArgsConstructor
public class BasicConfigAdvice {

    private final ConfigInfoService configInfoService;

    /**
     * 공통 값 유지할 때!
     */
    /*
    @ModelAttribute("siteConfig")
    public Map<String, String> getBasicConfig(){

//        Optional<Map<String, String>> opt = infoService.get("basic", new TypeReference<>() {});
//
//        Map<String, String> config = opt.orElseGet(() -> new HashMap<String, String>());
//        return config;
        Map<String, String> config = (Map<String, String>) configInfoService.get("basic", new TypeReference<>() {
        }).orElseGet(()-> new HashMap<String, String>());
        System.out.println("config" + config);

        return config;
    }
    */
    /**
     * 위에 꺼는 베이직콘피크가 널값일때 사이트가 안나올 수 있음 초기값을 설정해주어야 함!
     */

    @ModelAttribute("siteConfig")
    public BasicConfig getBasicConfig(){
        BasicConfig config = configInfoService.get("basic", BasicConfig.class).orElseGet(BasicConfig::new);
        return config;
    }

}
