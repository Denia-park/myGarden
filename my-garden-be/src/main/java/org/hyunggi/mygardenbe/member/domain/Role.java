package org.hyunggi.mygardenbe.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 유저 권한
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    Permission.ADMIN_READ,
                    Permission.ADMIN_UPDATE,
                    Permission.ADMIN_DELETE,
                    Permission.ADMIN_CREATE
            )
    ),
    ACTUATOR(Collections.emptySet());

    /**
     * 권한이 가지고 있는 구체적인 허용 권한 목록
     */
    private final Set<Permission> permissions;

    /**
     * 권한 목록을 SimpleGrantedAuthority 목록으로 변환
     * <br><br>
     * - 권한 목록에는 권한 이름과 권한이 가지고 있는 구체적인 허용 권한 목록이 포함됨
     *
     * @return SimpleGrantedAuthority 목록
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = extractAuthorityFromPermissions();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }

    /**
     * 권한이 가지고 있는 구체적인 허용 권한 목록을 SimpleGrantedAuthority 목록으로 변환
     *
     * @return SimpleGrantedAuthority 목록
     */
    private List<SimpleGrantedAuthority> extractAuthorityFromPermissions() {
        return getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionString()))
                .collect(Collectors.toList());
    }
}
