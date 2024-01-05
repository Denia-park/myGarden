package org.hyunggi.mygardenbe.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.member.domain.Member;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(length = 100, nullable = false)
    private String password;

    private MemberEntity(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public static MemberEntity of(Member member) {
        return new MemberEntity(
                member.getEmail(),
                member.getPassword()
        );
    }

    public Member toDomain() {
        return new Member(
                email,
                password
        );
    }
}
