package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.choongang.commons.entites.Base;
import org.choongang.file.entites.FileInfo;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member extends Base {
    @Id @GeneratedValue
    private Long seq;

    // 파일쪽과 연결할 수 있는 그룹아이디 추가
    @Column(length = 65, nullable = false)
    private String gid; // 파일을 가져오기 위해 그룹아이디가 필요함

    @Column(length = 80, nullable = false, unique = true)
    private String email;

    @Column(length = 40, nullable = false, unique = true)
    private String userId;

    @Column(length = 65, nullable = false)
    private String password;

    @Column(length = 40, nullable = false)
    private String name;

    @ToString.Exclude // 순환참조 방지
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Authorities> authorities = new ArrayList<>();

    /* 파일과 회원을 묶기 위해서 */
    @Transient
    private FileInfo profileImage;

//    @OneToMany(mappedBy = "fromMember", fetch = FetchType.LAZY)
//    private List<Follow> follows = new ArrayList<>();
//
//    @OneToMany(mappedBy = "toMember", fetch = FetchType.LAZY)
//    private List<Follow> followers = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<BoardData> favoriteBoardDataList = new ArrayList<>();
}
