package org.choongang.admin.config.controllers;

import lombok.Data;

/**
 * 커맨드 객체!
 */
@Data
public class BasicConfig {
    /**
     * null 값일 때 오류가 나기 때문에 초기값을 설정해주어야함!
     */
    private String siteTitle ="";
    private String siteDescription ="";
    private String siteKeywords ="";
    private int cssJsVersion =1;
    private String joinTerms ="";
}
