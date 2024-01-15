package org.hyunggi.mygardenbe.auth.jwt.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class Token {
    private String tokenText;
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;

    @Builder(access = AccessLevel.PRIVATE)
    private Token(final String tokenText, final TokenType tokenType, final boolean revoked, final boolean expired) {
        this.tokenText = tokenText;
        this.tokenType = tokenType;
        this.revoked = revoked;
        this.expired = expired;
    }

    public static Token of(final String tokenText, final TokenType tokenType, final boolean revoked, final boolean expired) {
        Assert.hasText(tokenText, "토큰은 null 혹은 빈 문자열이 될 수 없습니다.");
        Assert.notNull(tokenType, "토큰 타입은 null이 될 수 없습니다.");

        return Token.builder()
                .tokenText(tokenText)
                .tokenType(tokenType)
                .revoked(revoked)
                .expired(expired)
                .build();
    }

    public static Token createBearerToken(final String tokenText) {
        return of(tokenText, TokenType.BEARER, false, false);
    }

    public boolean isValid() {
        return !this.revoked && !this.expired;
    }

    public void revoke() {
        this.revoked = true;
        this.expired = true;
    }

    public void expire() {
        this.expired = true;
    }

    public boolean isSameTokenText(final String tokenText) {
        return this.tokenText.equals(tokenText);
    }

    public void refresh(final String refreshTokenText) {
        Assert.hasText(refreshTokenText, "토큰은 null 혹은 빈 문자열이 될 수 없습니다.");
        
        this.tokenText = refreshTokenText;
        this.revoked = false;
        this.expired = false;
    }
}
