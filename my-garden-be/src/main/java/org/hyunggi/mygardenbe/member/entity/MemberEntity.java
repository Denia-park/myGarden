package org.hyunggi.mygardenbe.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;
import org.hyunggi.mygardenbe.member.domain.Member;
import org.hyunggi.mygardenbe.member.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

/**
 * 유저 Entity
 */
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class MemberEntity extends BaseEntity implements UserDetails {
    /**
     * 유저 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 유저 이메일
     */
    @Column(length = 50, nullable = false, unique = true)
    private String email;

    /**
     * 유저 비밀번호
     */
    @Column(length = 100, nullable = false)
    private String password;

    /**
     * 유저 권한
     */
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Role role;

    /**
     * 유저 활성화 여부
     */
    private boolean enabled;

    @Builder(access = lombok.AccessLevel.PRIVATE)
    private MemberEntity(final String email, final String password, final Role role, final boolean enabled) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    /**
     * 유저 Entity 생성
     *
     * @param member          유저 Domain
     * @param passwordEncoder 비밀번호 암호화에 사용할 PasswordEncoder
     * @return 유저 Entity
     */
    public static MemberEntity of(final Member member, final PasswordEncoder passwordEncoder) {
        validate(member, passwordEncoder);

        return MemberEntity.builder()
                .email(member.getEmail())
                .password(passwordEncoder.encode(member.getPassword()))
                .role(member.getRole())
                .enabled(member.isEnabled())
                .build();
    }

    /**
     * 유저 Entity 생성 인자 유효성 검증
     *
     * @param member          유저 Domain
     * @param passwordEncoder 비밀번호 암호화에 사용할 PasswordEncoder
     */
    private static void validate(final Member member, final PasswordEncoder passwordEncoder) {
        if (member == null) {
            throw new IllegalArgumentException("Member는 null이 될 수 없습니다.");
        }

        if (passwordEncoder == null) {
            throw new IllegalArgumentException("PasswordEncoder는 null이 될 수 없습니다.");
        }
    }

    /**
     * 해당 유저 Entity의 권한 목록 반환
     *
     * @return 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getAuthorities();
    }

    /**
     * 해당 유저 Entity의 비밀번호 반환
     *
     * @return 비밀번호
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * 해당 유저 Entity의 이메일 반환
     *
     * @return 이메일
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * 해당 유저 Entity의 계정 만료 여부 반환
     *
     * @return 계정 만료 여부
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 해당 유저 Entity의 계정 잠금 여부 반환
     *
     * @return 계정 잠금 여부
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 해당 유저 Entity의 비밀번호 만료 여부 반환
     *
     * @return 비밀번호 만료 여부
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 해당 유저 Entity의 활성화 여부 반환
     *
     * @return 활성화 여부
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
