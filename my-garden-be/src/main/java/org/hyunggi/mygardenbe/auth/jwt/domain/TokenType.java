package org.hyunggi.mygardenbe.auth.jwt.domain;

import lombok.Getter;

@Getter
public enum TokenType {
    BEARER("Bearer ");

    private final String parseText;

    TokenType(final String parseText) {
        this.parseText = parseText;
    }
}
