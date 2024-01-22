package org.choongang.member.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.choongang.member.Authority;

@Data
@Entity
// 회원 1명 당 동일한 권한은 1개만 회원번호와 권한을 묶어서 유니크 제약조건을 부여함
@Table(indexes = @Index(name = "uq_member_authority",
        columnList = "member_seq, authority", unique = true))
public class Authorities {
    @Id @GeneratedValue
    private Long seq;

    // 회원은 1명 권한은 여러개...
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    private Member member;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Authority authority;
}
