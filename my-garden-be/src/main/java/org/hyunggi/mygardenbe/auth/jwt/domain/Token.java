package org.hyunggi.mygardenbe.auth.jwt.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * 토큰 Domain
 */
@Getter
public class Token {
    /**
     * 토큰 값
     */
    private String tokenText;
    /**
     * 토큰 타입 - Bearer
     */
    private TokenType tokenType;
    /**
     * 토큰 폐기 여부
     */
    private boolean revoked;
    /**
     * 토큰 만료 여부
     */
    private boolean expired;

    @Builder(access = AccessLevel.PRIVATE)
    private Token(final String tokenText, final TokenType tokenType, final boolean revoked, final boolean expired) {
        this.tokenText = tokenText;
        this.tokenType = tokenType;
        this.revoked = revoked;
        this.expired = expired;
    }

    /**
     * 토큰 Doamin 생성
     *
     * @param tokenText 토큰 값
     * @param tokenType 토큰 타입
     * @param revoked   폐기 여부
     * @param expired   만료 여부
     * @return 토큰 Domain
     */
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

    /**
     * Bearer 타입의 토큰 생성
     *
     * @param tokenText 토큰 값
     * @return Bearer 토큰 Domain
     */
    public static Token createBearerToken(final String tokenText) {
        return of(tokenText, TokenType.BEARER, false, false);
    }

    /**
     * 토큰이 유효한지 확인
     *
     * @return 유효 여부
     */
    public boolean isValid() {
        return !this.revoked && !this.expired;
    }

    /**
     * 토큰을 폐기한다.
     */
    public void revoke() {
        this.revoked = true;
        this.expired = true;
    }

    /**
     * 토큰을 만료한다.
     */
    public void expire() {
        this.expired = true;
    }

    /**
     * 토큰 값이 같은지 확인
     *
     * @param tokenText 비교할 토큰 값
     * @return 받아온 토큰과 현재 토큰이 같은지 여부
     */
    public boolean isSameTokenText(final String tokenText) {
        return this.tokenText.equals(tokenText);
    }

    /**
     * 토큰을 갱신한다.
     *
     * @param refreshTokenText 갱신할 토큰 값
     */
    public void refresh(final String refreshTokenText) {
        Assert.hasText(refreshTokenText, "토큰은 null 혹은 빈 문자열이 될 수 없습니다.");

        this.tokenText = refreshTokenText;
        this.revoked = false;
        this.expired = false;
    }
}
