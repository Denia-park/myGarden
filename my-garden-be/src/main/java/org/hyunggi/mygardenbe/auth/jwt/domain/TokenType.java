package org.hyunggi.mygardenbe.auth.jwt.domain;

import lombok.Getter;

/**
 * 토큰 타입
 */
@Getter
public enum TokenType {
    /**
     * Bearer 타입
     */
    BEARER("Bearer ");

    /**
     * parsing시에 사용할 Text
     */
    private final String parseText;

    TokenType(final String parseText) {
        this.parseText = parseText;
    }

    /**
     * parseText 길이 반환
     *
     * @return parseText 길이
     */
    public int getParseTextLength() {
        return this.parseText.length();
    }
}
