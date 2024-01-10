package org.hyunggi.mygardenbe.auth.jwt.domain;

import org.springframework.util.Assert;

public class Token {
    private String tokenText;
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;

    private Token(final String tokenText, final TokenType tokenType, final boolean revoked, final boolean expired) {
        validateConstructor(tokenText, tokenType);

        this.tokenText = tokenText;
        this.tokenType = tokenType;
        this.revoked = revoked;
        this.expired = expired;
    }

    private void validateConstructor(final String tokenText, final TokenType tokenType) {
        Assert.hasText(tokenText, "토큰은 null 혹은 빈 문자열이 될 수 없습니다.");
        Assert.notNull(tokenType, "토큰 타입은 null이 될 수 없습니다.");
    }

    public static Token createBearerToken(final String tokenText) {
        return new Token(tokenText, TokenType.BEARER, false, false);
    }

    public boolean isValid() {
        return !this.revoked && !this.expired;
    }

    public void revoke() {
        this.revoked = true;
        this.expired = true;
    }

    public boolean isSameTokenText(final String tokenText) {
        return this.tokenText.equals(tokenText);
    }

    public void refresh(final String refreshTokenText) {
        this.tokenText = refreshTokenText;
        this.revoked = false;
        this.expired = false;
    }
}
