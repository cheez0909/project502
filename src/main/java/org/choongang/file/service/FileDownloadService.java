package org.choongang.file.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.file.entites.FileInfo;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class FileDownloadService {
    private final FileInfoService infoService;
    private final HttpServletResponse response;

    public void download(Long seq){
        FileInfo data = infoService.get(seq);
        // String fileName = data.getFileName(); // 파일이름을 가져옴
        String filePath = data.getFilePath();

        /* 파일명을 2바이트로 인코딩 S(윈도우즈 시스템에서 한글 깨짐 방지 */
        String fileName = null;

        try {
            fileName = new String(data.getFileName().getBytes(), "ISO8859_1");
        } catch (UnsupportedEncodingException e) { }
        /* 파일명 인코딩 E */

        File file = new File(filePath);
        // new io -> 버퍼가 탑재되어있음..?
        try(FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis)){
            OutputStream out = response.getOutputStream(); // 응답 body에 출력 , 1바이트, 바이너리 데이터

            response.setHeader("Content-Disposition", "attachment; filename="+fileName);
            // 타입을 정확히 알려주는 게 좋지만 파일 기능은 범용적으로 쓰기 떄문에 정확히 어떤 파일인지 알 수 없음
            // application/octet-stream 범용적으로 쓰겠다는 뜻
            response.setHeader("Content-Type", "application/octet-stream");
            // 브라우저가 끊지 않도록 만료시간을 늘림(파일 용량이 클 때)
            response.setIntHeader("Expires", 0); // 만료시간 없음

            // 브라우저가 캐싱을 함 304
            // 파일명이 동일하면 캐싱을 하기 때문에 새로 다운이 안됨 -> 그러나 같은 파일명이지만 내용이 다를 수도 있음
            // 따라서 캐싱을 하면 안됨
            response.setHeader("Cache-Control", "must-revalidate");
            response.setHeader("Pragma", "public"); // 아주 옛날 브라우저는 이걸로 인식함 위에 있는 것과 동일 : 캐시안함
            response.setHeader("Content-Length", String.valueOf(file.length()));

            while (bis.available() > 0){
                out.write(bis.read());
            }

            out.flush(); // 윈도우는 2바이트 서버는 3바티임 한글의 파일명일때, 파일명을 2바이트로 바꿔줘야함

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
