package org.hyunggi.mygardenbe.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;
import org.hyunggi.mygardenbe.member.domain.Member;
import org.hyunggi.mygardenbe.member.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class MemberEntity extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(length = 100, nullable = false)
    private String password;
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Role role;

    private boolean enabled;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
