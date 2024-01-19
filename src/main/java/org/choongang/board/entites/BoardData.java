package org.choongang.board.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entites.Base;
import org.choongang.commons.entites.BaseMember;
import org.choongang.file.entites.FileInfo;
import org.choongang.member.entities.Member;

import java.util.List;
import java.util.UUID;

@Data @Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_boardData_basic", columnList = "notice DESC, createdAt DESC")
})
public class BoardData extends Base implements AuthCheck{

    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberSeq")
    private Member member;

    @Column(length = 65, nullable = false)
    private String gid = UUID.randomUUID().toString();

    @Column(length = 60)
    private String category; // 분류

    @Column(length = 40, nullable = false)
    private String poster; // 작성자

    private String guestPw; // 비회원 비밀번호

    private boolean notice; // 공지글 여부 - true : 공지글 -> notice인것을 먼저 정렬

    @Column(length = 255, nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    private int viewCount; // 조회 수
    private int commentCount; // 댓글 수 -> getTotal

    @Column(length = 20)
    private String ip; // IP주소

    @Column(length = 255)  // 효과적인 db사용을 위해 길이를 제한함
    private String ua; // user-agent : 브라우저 정보

    @Column(length = 100)
    private String text1; // 추가 필드 : 한줄 텍스트
    @Column(length = 100)
    private String text2; // 추가 필드 : 한줄 텍스트
    @Column(length = 100)
    private String text3; // 추가 필드 : 한줄 텍스트

    @Lob
    private String longText1; // 추가 필드 : 여러줄 텍스트

    @Lob
    private String longText2; // 추가 필드 : 여러줄 텍스트

    @Lob
    private String longText3; // 추가 필드 : 여러줄 텍스트

    private Long num1; // 추가 필드 : 정수
    private Long num2; // 추가 필드 : 정수
    private Long num3; // 추가 필드 : 정수

    @Transient
    private List<FileInfo> editorFiles;

    @Transient
    private List<FileInfo> attachFiles;
//    @ManyToOne
//    @JoinColumn(name = "boardData_id")
//    private Member member;

    @Transient
    private boolean editable; // 수정 가능 여부

    @Transient
    private boolean deletable; // 삭제 가능 여부

    @Transient
    private boolean mine; // 게시글 소유자

    @Transient
    private boolean showEditButton; // 수정 버튼 노출 여부 : 노출에 대한 부분이지 버튼 자체가 아님

    @Transient
    private boolean showDeleteButton; // 삭제 버튼 노출 여부 : 노출에 대한 부분이지 버튼 자체가 아님

    @Transient
    private List<CommentData> comments; // 댓글 목록
}
