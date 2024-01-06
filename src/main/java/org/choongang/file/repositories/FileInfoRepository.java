package org.choongang.file.repositories;

import org.choongang.file.entites.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long>, QuerydslPredicateExecutor<FileInfo> {

    // 정렬은 어떻게해? -> 나중에..
    List<FileInfo> findByGid(String gid);
    List<FileInfo> findByLocation(String location);
    List<FileInfo> findByGidAndLocation(String gid, String location);

}
