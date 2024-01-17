package org.hyunggi.mygardenbe.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @ParameterizedTest
    @MethodSource("roleAuthoritiesProvider")
    @DisplayName("getAuthorities 메서드를 호출하면, 해당 Role의 권한을 SimpleGrantedAuthority로 반환한다.")
    void getAuthorities(Role role, List<SimpleGrantedAuthority> expectedAuthorities) {
        //given, when
        List<SimpleGrantedAuthority> authorities = role.getAuthorities();

        //then
        assertThat(authorities).containsExactlyInAnyOrderElementsOf(expectedAuthorities);
    }

    public static Stream<Arguments> roleAuthoritiesProvider() {
        return Stream.of(
                Arguments.of(Role.USER, List.of(new SimpleGrantedAuthority("ROLE_USER"))),
                Arguments.of(Role.ADMIN, List.of(
                                new SimpleGrantedAuthority("ROLE_ADMIN"),
                                new SimpleGrantedAuthority("ADMIN:READ"),
                                new SimpleGrantedAuthority("ADMIN:UPDATE"),
                                new SimpleGrantedAuthority("ADMIN:CREATE"),
                                new SimpleGrantedAuthority("ADMIN:DELETE")
                        )
                ));
    }

    @ParameterizedTest
    @MethodSource("rolePermissionProvider")
    @DisplayName("getPermissions 메서드를 호출하면, 해당 Role의 권한을 반환한다.")
    void getPermissions(Role role, Set<Permission> expectedPermissions) {
        //given, when
        Set<Permission> permissions = role.getPermissions();

        //then
        assertThat(permissions).isEqualTo(expectedPermissions);
    }

    public static Stream<Arguments> rolePermissionProvider() {
        return Stream.of(
                Arguments.of(Role.USER, Collections.emptySet()),
                Arguments.of(Role.ADMIN, Set.of(
                                Permission.ADMIN_READ,
                                Permission.ADMIN_UPDATE,
                                Permission.ADMIN_DELETE,
                                Permission.ADMIN_CREATE
                        )
                ));
    }
}
