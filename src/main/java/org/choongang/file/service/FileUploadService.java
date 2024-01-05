package org.choongang.file.service;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.choongang.commons.Utils;
import org.choongang.configs.FileProperties;
import org.choongang.file.entites.FileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileUploadService {

    private final FileInfoRepository fileInfoRepository;
    private final FileProperties fileProperties;
    private final FileInfoService fileInfoService;
    private final Utils utils; // 썸네일 추가 가져오기
    public List<FileInfo> upload(MultipartFile[] files, String gid, String location, boolean imageOnly){
        /**
         * 1. 파일 정보 저장
         * 2. 서버쪽에 파일 업로드
         */

        /* 파일 정보 저장 S */
        // 자바에서 제공되는 랜덤 아이디(유니크)
        gid = StringUtils.hasText(gid) ? gid : UUID.randomUUID().toString();
        String uploadPath = fileProperties.getPath(); // 파일 업로드 기본 경로
        String thumbPath = uploadPath + "thumbs/"; // 썸네일 업로드 기본 경로
        

        List<int[]> thumbsSize = utils.getThumbSize(); // 썸네일 사이즈

        List<FileInfo> uploadedFiles = new ArrayList<>(); // 파일 정보를 저장하는 리스트

        for(MultipartFile file : files){

            String fileName = file.getOriginalFilename(); // 오리지널 파일명
            String contentType = file.getContentType(); // 파일 타입
            String extension = fileName.substring(fileName.lastIndexOf(".")); // 확장자 명
            String fileType = file.getContentType(); // 파일 종류 : 이미지, 문서 등

            FileInfo fileInfo = FileInfo.builder()
                    .gid(gid)
                    .location(location)
                    .fileName(fileName)
                    .fileType(fileType)
                    .extension(extension)
                    .build();

            fileInfoRepository.saveAndFlush(fileInfo);
            /* 파일 정보 저장 E */

            /* 파일 업로드 처리 S */
            long seq = fileInfo.getSeq();
            File dir = new File(uploadPath + (seq % 10));
            if(!dir.exists()){
                // 디렉토리가 없다면 생성
                dir.mkdir();
            }

            File uploadFile = new File(dir, seq + extension); // 파일을 서버 폴더에 저장

            try {
                file.transferTo(uploadFile); // 파일을 uploadFile 라는 목적지로 전송

                /* 썸네일 이미지 처리 S */
                if(fileType.indexOf("image/")!=-1 && thumbsSize != null){
                    File thumbDir = new File(thumbPath + (seq % 10L) + "/" + seq);
                    if(!thumbDir.exists()){
                        thumbDir.mkdirs();
                    }
                    for(int[] sizes : thumbsSize){
                        String thumbFileName = sizes[0] + "_" + sizes[1] + "_" + seq + extension;

                        File thumb = new File(thumbDir, thumbFileName);

                        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
                        System.out.println(Arrays.toString(sizes));

                        /**
                         * 워터마크 처리도 가능함
                         */
                        Thumbnails.of(uploadFile)
                                .size(sizes[0], sizes[1])
                                .toFile(thumb);
                    }
                }
                /* 썸네일 이미지 처리 E */

                fileInfoService.addFileInfo(fileInfo); // 파일 추가 정보를 저장

                uploadedFiles.add(fileInfo); // 파일 정보를 저장함

            } catch (IOException e){
                e.printStackTrace();
                fileInfoRepository.delete(fileInfo); // 업로드 실패시에는 파일 정보 제거
                fileInfoRepository.flush();
            }
            /* 파일 업로드 처리 E */
        }
        return uploadedFiles;
    }

    /**
     * 업로드 완료 처리
     * @param gid
     */
    public void processDone(String gid){
        List<FileInfo> files = fileInfoRepository.findByGid(gid);
        if(files == null){
            return;
        }
        files.forEach(file -> file.setDone(true));
        fileInfoRepository.flush();
    }
}
