package org.choongang.file.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entites.BaseMember;

import java.util.List;
import java.util.UUID;

/**
 * 삭제 방지를 위해 작성자와 수정자를 체크해야함
 */
@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = { // 조회가 많은 부분에 인덱스 추가할 수 있음
        @Index(name = "idx_fInfo_gid", columnList = "gid"), // gid ASC 방향성 추가 가능
        @Index(name = "idx_fInfo_gid_loc", columnList = "gid, location")
})
public class FileInfo extends BaseMember {
    @Id @GeneratedValue
    private Long seq; // 파일 등록 번호, 서버에 업로드 하는 파일명 기준
    @Column(length = 65, nullable = false)
    private String gid = UUID.randomUUID().toString(); // 중복되지 않는 아이디를 랜덤으로 문자열로 만들어줌
    @Column(length = 65)
    private String location; // 아이콘 이미지, 상세이미지, 첨부이미지등등 -> 하나의 그룹안에서 용도가 다를 수 있음
    @Column(length = 80)
    private String fileName; // 원래 파일명
    @Column(length = 30)
    private String extension; // 확장자

    @Column(length = 65)
    private String fileType; // 파일 타입 이미지인지...

    /**
     * DB아니고 자바에서만 관리하는 (?)필드
     * 영속성 컨텍스트에 저장하지 않도록 지정하는 데 사용
     */
    @Transient
    private String filePath; // 서버에 실제 올라간 경로

    @Transient
    private String fileUrl; // 브라우저 주소창에 입력해서 접근할 수 있는 경로

    @Transient
    private List<String> thumbsPath; // 썸네일 이미지 경로 : 삭제할 때 필요함(서버에)

    @Transient
    private List<String> thumbsUrl; // 브라우저 주소창에 입력해서 접근할 수 있는 경로

    private boolean done; // 파일 저장 성공 여부
}
