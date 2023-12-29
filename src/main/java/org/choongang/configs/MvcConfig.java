package org.choongang.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableConfigurationProperties(FileProperties.class)
@EnableJpaAuditing
public class MvcConfig implements WebMvcConfigurer {
    // 아답터 형태의 인터페이스 제공...?

    @Autowired
    private FileProperties fileProperties;


    /**
     * 메세지 소스 설정
     */
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setDefaultEncoding("UTF-8");
        ms.setBasenames("messages.commons", "messages.errors", "messages.validations"); // 메세지 파일 명 입력
        return ms;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(registry);
        registry.addResourceHandler(fileProperties.getUrl()+"**")
                .addResourceLocations("file:///"+fileProperties.getPath());
    }

    /**
     * form 양식에서 hidden값으로 name에 _method를 넣고
     * value값에 PATCH, PUT, DELETE값을 넣으면 사용할 수 있음
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter();
    }
}
