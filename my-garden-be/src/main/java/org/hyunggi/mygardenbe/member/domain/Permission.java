package org.hyunggi.mygardenbe.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 유저의 구체적인 허용 권한
 */
@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("ADMIN:READ"),
    ADMIN_UPDATE("ADMIN:UPDATE"),
    ADMIN_CREATE("ADMIN:CREATE"),
    ADMIN_DELETE("ADMIN:DELETE");

    /**
     * 권한 문자열
     */
    private final String permissionString;
}
