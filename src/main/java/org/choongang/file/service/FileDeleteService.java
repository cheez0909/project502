package org.choongang.file.service;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.UnAuthorizedException;
import org.choongang.file.entites.FileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileDeleteService {
    // 파일이 있어야 삭제 가능함
    // 파일 정보를 불러오자!
    private final FileInfoService fileInfoService;
    private final MemberUtil memberUtil;
    private final FileInfoRepository repository;

    public void delete(Long seq){
        FileInfo fileInfo = fileInfoService.get(seq);

        // 파일 삭제 권한 체크
        Member member = memberUtil.getMember();
        String createdBy = fileInfo.getCreateBy();
        // 관리자가 아니지만, 작성자가 있고, 작성자일때
        if(!memberUtil.isLogin()
                || StringUtils.hasText(createdBy) && !createdBy.equals(member.getUserId())){
            throw new UnAuthorizedException(Utils.getMessage("Not.your.file", "errors"));
        }
        File file = new File(fileInfo.getFilePath());
        if(file.exists()) file.delete();

        List<String> thumbsPath = fileInfo.getThumbsPath();
        if(thumbsPath != null){
            for(String path:thumbsPath){
                File thumbFile = new File(path);
                if(thumbFile.exists()) thumbFile.delete();
            }
        }
        repository.delete(fileInfo);
        repository.flush();
    }
}
