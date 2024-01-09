package org.choongang.file.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.UnAuthorizedException;
import org.choongang.file.entites.FileInfo;
import org.choongang.file.entites.QFileInfo;
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
    /**
     * 파일 삭제 로직
     * 1. 파일이 있는 지 확인
     * 2. 삭제할 수 있는 권한 확인 (회원 정보 확인 관리자 인지 or 본인이 올린 파일인지)
     */

    private final FileInfoService fileInfoService;
    private final MemberUtil memberUtil;
    private final FileInfoRepository repository;

    public void delete(Long seq){

        FileInfo data = fileInfoService.get(seq);

        // 파일 삭제 권한 체크
        Member member = memberUtil.getMember();
        // createdBy가 존재하면 회원이 올린 파일임
        String createdBy = data.getCreateBy();
        // 로그인이 되어있지 않거나 관리자가 아니면서 작성자도 아닐 때는 에러 발생
        if (StringUtils.hasText(createdBy) && (!memberUtil.isLogin() || (!memberUtil.isAdmin() && StringUtils.hasText(createdBy)
                && !createdBy.equals(member.getUserId())))) {
            throw new UnAuthorizedException(Utils.getMessage("Not.your.file", "errors"));
        }
        File file = new File(data.getFilePath()); // 경로에 대한 참조값을 얻음
        if(file.exists()) file.delete();

        List<String> thumbsPath = data.getThumbsPath();
        if(thumbsPath != null){
            for(String path:thumbsPath){
                File thumbFile = new File(path);
                if(thumbFile.exists()) thumbFile.delete();
            }
        }
        repository.delete(data);
        repository.flush();
    }

    /**
     * 위치, gid값으로 삭제하는 메서드
     * @param location : 위치
     * @param gid : 그룹 아이디
     */
    public void delete(String location, String gid){
        QFileInfo fileInfo = QFileInfo.fileInfo;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(fileInfo.gid.eq(gid));

        if(StringUtils.hasText(location)){
            builder.and(fileInfo.location.eq(location));
        }

        List<FileInfo> items = (List<FileInfo>) repository.findAll(builder);
        items.forEach(i->delete(i.getSeq()));
    }

    public void delete(String gid){
        delete(null, gid);
    }
}
