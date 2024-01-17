package org.hyunggi.mygardenbe.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("ADMIN:READ"),
    ADMIN_UPDATE("ADMIN:UPDATE"),
    ADMIN_CREATE("ADMIN:CREATE"),
    ADMIN_DELETE("ADMIN:DELETE");

    private final String permissionString;
}
