package org.choongang.file.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.choongang.configs.FileProperties;
import org.choongang.file.entites.FileInfo;
import org.choongang.file.entites.QFileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileInfoService {
    /**
     * gid로 조회, location조회, 낱개로 조회
     */
    private final FileProperties fileProperties;

    private final HttpServletRequest request;

    private final FileInfoRepository repository;
    public FileInfo get(Long seq){
        FileInfo fileInfo = repository.findById(seq).orElseThrow(FileNotFoundException::new);
        addFileInfo(fileInfo); // 파일 추가정보 처리

        return fileInfo;
    }

    /**
     * 파일 추가 정보 처리
     *      - 파일 서버 경로(filePath)
     *      - 파일 URL(fileUrl)
     * @param fileInfo
     */
    public void addFileInfo(FileInfo fileInfo) {
        /* 파일 경로, URL S */
        long seq = fileInfo.getSeq();
        long dir = seq % 10L;
        String fileName = seq + fileInfo.getExtension();

        String path = fileProperties.getPath() + dir + "/" + fileName; // 저장되는 서버 경로
        String url =request.getContextPath() + fileProperties.getUrl() + dir + "/" + fileName; // 요청할 url


        fileInfo.setFileUrl(url);
        fileInfo.setFilePath(path);
        /* 파일 경로, URL E */

        /* 썸네일 경로, URL S */
        List<String> thumbsPath = new ArrayList<>();
        List<String> thumbsUrl = new ArrayList<>();

        String thumbDir = getThumbDir(seq);
        String thumbUrl = getThumbUrl(seq);

        File _thumbDir = new File(thumbDir);
        if(_thumbDir.exists()){
            for(String thumbFileName : _thumbDir.list()){
                thumbsPath.add(thumbDir + "/" + thumbFileName);
                thumbsUrl.add(thumbUrl + "/" + thumbFileName);
            }
        }

        fileInfo.setThumbsPath(thumbsPath);
        fileInfo.setThumbsUrl(thumbsUrl);
        /* 썸네일 경로, URL E */


//        FileInfo fileInfo1 = new ModelMapper().map(fileInfo, FileInfo.class);
//        fileInfo.setFilePath(fileInfo.getFilePath());
//        fileInfo.setFileUrl(fileInfo.getFileUrl());
    }



    /**
     * 파일 목록 조회
     * @param gid
     * @param location
     * @param mode : All이면 전체, done이면 완료된 것만, UNDONE: 미완료된 파일
     * @return
     */
    public List<FileInfo> getList(String gid, String location, String mode){
        // gid는 무조건 있어야 되고 location은 없을 수도 있음
        QFileInfo fileInfo = QFileInfo.fileInfo;
        mode = StringUtils.hasText(mode) ? mode : "ALL";
        BooleanBuilder builder = new BooleanBuilder(); // 조건이 여러개 일 때 필요함 and or 조건
        builder.and(fileInfo.gid.eq(gid)); // gid는 필수로 넣어줘야함

        if(StringUtils.hasText(location)){
            builder.and(fileInfo.location.eq(location));
        }

        if(!mode.equals("ALL")){
            builder.and(fileInfo.done.eq(mode.equals("DONE")));
        }

        List<FileInfo> items = (List<FileInfo>) repository.findAll(builder, Sort.by(asc("createdAt")));

        items.forEach(this::addFileInfo);

        return items;
    }

    /**
     * 전체 조회
     */
    public List<FileInfo> getList(String gid){
        return getList(gid, null, "ALL");
    }

    public List<FileInfo> getList(String gid, String location){
        return getList(gid, location, "ALL");
    }


    /**
     * 완료된 파일만 가져오기
     */
    public List<FileInfo> getListDone(String gid){
        return getList(gid, null, "DONE");
    }

    public List<FileInfo> getListDone(String gid, String location){
        return getList(gid, location, "DONE");
    }


    public String[] getThumb(Long seq, int width, int height) {
        FileInfo fileInfo = get(seq);

        String fileType = fileInfo.getFileType(); // 파일이 이미지인지 체크
        if (fileType.indexOf("image/") == -1) {
            return null;
        }
        String fileName = seq + fileInfo.getFileName();
        String thumbDir = getThumbDir(seq);
        File _thumbDir = new File(thumbDir);
        // 썸네일 이미지가 없는 경우
        if (!_thumbDir.exists()) {
            // 경로 만들기
            _thumbDir.mkdirs();
        }

        String thumbPath = String.format("%s/%d_%d_%s", thumbDir, width, height, fileName);
        File _thumbPath = new File(thumbPath);
        // 썸네일 경로에 없으면
        if (!_thumbPath.exists()) {
            try {
                // 만들기
                Thumbnails.of(new File(fileInfo.getFilePath()))
                        .size(width, height).toFile(_thumbPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String thumbUrl = String.format("%s/%d_%d_%s", getThumbUrl(seq), width, height, fileName);
        return new String[] {thumbPath, thumbUrl};
    }
    public String getThumbDir(long seq){
        String thumbDirCommon = "thumbs/" + (seq % 10L) + "/" + seq;
        return fileProperties.getPath() + thumbDirCommon;
    }
    public String getThumbUrl(long seq){
        String thumbDirCommon = "thumbs/" + (seq % 10L) + "/" + seq;
        return fileProperties.getUrl() + thumbDirCommon;
    }
}
