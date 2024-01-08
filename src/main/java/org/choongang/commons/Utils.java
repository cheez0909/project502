package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.file.service.FileInfoService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Component // 스프링 관리 객체
@RequiredArgsConstructor
public class Utils {

    /**
     * 매개변수에 넣지 않은 이유
     * 제공된 코드에서는 클래스 private final HttpServletRequest request;에
     * 인스턴스 변수로 가 선언되어 있으며 @RequiredArgsConstructor를 통해 생성자 주입을 통해 주입되고 있습니다
     *  이는 클래스 HttpServletRequest의 인스턴스가 Utils생성될 때 객체가
     *  제공될 것으로 예상된다는 것을 의미합니다.
     */
    private final HttpServletRequest request;
    private final HttpSession session;
    private final FileInfoService fileInfoService;

    /**
     * 스태틱 구간에 초기화를 시킴
     */
    private static final ResourceBundle commonsBundle;
    private static final ResourceBundle validationsBundle;
    private static final ResourceBundle errorsBundle;

    static {
        commonsBundle = ResourceBundle.getBundle("messages.commons");
        validationsBundle = ResourceBundle.getBundle("messages.validations");
        errorsBundle = ResourceBundle.getBundle("messages.errors");
    }

    /**
     * 모바일과 pc 구분하기
     */
    public boolean isMobile(){
        /**
         * 모바일 수동 전환 모드 체크
         * 값이 있으면 모바일 없으면 PC
         */
        String device = (String) session.getAttribute("device");
        if(StringUtils.hasText(device)){
            return device.equals("MOBILE");
        }

        /**
         * 요청헤더에서 user-Agent 정보 불러오기
         */
        String ua = request.getHeader("User-Agent");
        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*";
        return ua.matches(pattern);
    }

    /**
     * 경로 설정
     */
    public String tpl(String path) {
        String prefix = isMobile() ? "mobile/" : "front/";
        return prefix + path;
    }

    /**
     *
     * @param code : 키값
     * @param type : 메세지 타입 commons, errors, validations
     * @return
     * 타입에 따라서 다른 번들을 가져옴
     */
    public static String getMessage(String code, String type){
        type = StringUtils.hasText(type) ? type : "validations";

        ResourceBundle bundle = null;

        if(type.equals("commons")){
            bundle =commonsBundle;
        } else if(type.equals("errors")){
            bundle=errorsBundle;
        } else {
            bundle=validationsBundle;
        }
        return bundle.getString(code);
    }

    /**
     * validationsBundle이 가장 많이 사용되기 때문에
     * 초기화 시킴
     */
    public static String getMessage(String code){

        return getMessage(code, null);
    }

    /**
     * \n 또는 \r\n -> <br>
     * @param str
     * @return
     */
    public String nl2br(String str){
       // str = Objects.requireNonNullElse(str, "");
        str = str.replaceAll("\\n", "<br>")
                .replaceAll("\\r", "");
        return str;
    }

    /**
     * 썸네일 사이즈 가져 오도록 추가
     */
    public List<int[]> getThumbSize(){
        BasicConfig basicConfig = (BasicConfig) request.getAttribute("siteConfig");
        String thumbSize = basicConfig.getThumbSize(); // \r\n
        String[] thumsSize = basicConfig.getThumbSize().split("\\n"); // \r\n -> 줄개행문자 \n으로 자르고 \r을 삭제
        List<int[]> data = Arrays.stream(thumsSize)
                .filter(StringUtils::hasText) // 공백이 아닐때
                .map(s -> s.replaceAll("\\s+", "")) // 한개 이상의 공백
                .map(this::toConvert).toList();
        return data;
    }

    private int[] toConvert(String size) {
        size = size.trim();

        int[] data = Arrays.stream(size.replaceAll("\\r", "")
                .toUpperCase().split("X")).mapToInt(Integer::parseInt).toArray();
        return data;
    }

    /**
     * 썸네일을 이미지 태그로 변환해서 타임리프에서 utext 속성으로 사용할 수 있다!
     * @param seq : id
     * @param width : 가로
     * @param height : 세로
     * @param className : 나중에 css를 사용하기 위해 클래스 네임이 필요할 수 있으니 넣어준 것!
     * @return
     */
    public String printThumb(long seq, int width, int height, String className){
        String[] data = fileInfoService.getThumb(seq, width, height);
        if(data != null){
            // 클래스 네임이 있을땐 클래스 네임 넣어줌
            String cls = StringUtils.hasText(className) ? " class='"+className+"'" : "";
            String image = String.format("<img src='%s'%s>", data[1], cls);
            return image;
        }
        return "";
    }

    public String printThumb(long seq, int width, int height){
        return printThumb(seq, width, height, null);
    }
}