package org.hyunggi.mygardenbe.auth.jwt.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TokenTest {
    @Test
    @DisplayName("정적 메서드 of를 통해 객체를 생성할 수 있다.")
    void of() {
        //given
        final String tokenText = "tokenText";
        final TokenType tokenType = TokenType.BEARER;
        final boolean revoked = false;
        final boolean expired = false;

        //when
        final Token token = Token.of(tokenText, tokenType, revoked, expired);

        //then
        assertThat(token).isNotNull();
        assertEquals(tokenText, token.getTokenText());
        assertEquals(tokenType, token.getTokenType());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("정적 메서드 of의 입력으로 tokenText가 null 혹은 빈 문자열이면 예외가 발생한다.")
    void of_nullAndEmptyTokenText(final String tokenText) {
        //when, when
        assertThatThrownBy(() -> {
            Token.of(tokenText, TokenType.BEARER, false, false);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토큰은 null 혹은 빈 문자열이 될 수 없습니다.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("정적 메서드 of의 입력으로 tokenType이 null이면 예외가 발생한다.")
    void of_nullTokenType(final TokenType tokenType) {
        //when, when
        assertThatThrownBy(() -> {
            Token.of("tokenText", tokenType, false, false);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토큰 타입은 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("토큰을 생성한다.")
    void createBearerToken() {
        //given
        final String tokenText = "tokenText";

        //when
        final Token token = Token.createBearerToken(tokenText);

        //then
        assertThat(token).isNotNull();
        assertEquals(tokenText, token.getTokenText());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
    }

    @Test
    @DisplayName("토큰이 유효한지 확인한다.")
    void isValid() {
        //given
        final Token token = Token.createBearerToken("tokenText");

        //when
        final boolean valid = token.isValid();

        //then
        assertTrue(valid);
    }

    @Test
    @DisplayName("토큰을 만료시키면, expired가 true로 변경된다.")
    void expire() {
        //given
        final Token token = Token.createBearerToken("tokenText");

        //when
        token.expire();

        //then
        assertTrue(token.isExpired());
    }

    @Test
    @DisplayName("토큰을 취소시키면, revoked와 expired가 true로 변경된다.")
    void revoke() {
        //given
        final Token token = Token.createBearerToken("tokenText");

        //when
        token.revoke();

        //then
        assertTrue(token.isRevoked());
        assertTrue(token.isExpired());
    }

    @Test
    @DisplayName("revoked가 true인 토큰은 유효하지 않다.")
    void isValidWhenRevoked() {
        //given
        final Token token = Token.createBearerToken("tokenText");
        token.revoke();

        //when
        final boolean valid = token.isValid();

        //then
        assertFalse(valid);
    }

    @Test
    @DisplayName("expired가 true인 토큰은 유효하지 않다.")
    void isValidWhenExpired() {
        //given
        final Token token = Token.createBearerToken("tokenText");
        token.expire();

        //when
        final boolean valid = token.isValid();

        //then
        assertFalse(valid);
    }

    @Test
    @DisplayName("토큰의 text와 인자로 받은 text가 같은지 확인한다.")
    void isSameTokenText() {
        //given
        final String tokenText = "tokenText";
        final Token token = Token.createBearerToken(tokenText);

        //when
        final boolean sameTokenText = token.isSameTokenText(tokenText);

        //then
        assertTrue(sameTokenText);
    }

    @Test
    @DisplayName("refresh()를 하면, 주어진 text로 토큰의 text를 변경하고 만료된 Token을 다시 활성화시킨다.")
    void refresh() {
        //given
        final Token token = Token.createBearerToken("tokenText");
        token.expire();
        final boolean isOldTokenValid = token.isValid();

        final String refreshTokenText = "refreshTokenText";

        //when
        token.refresh(refreshTokenText);
        final boolean isNewTokenValid = token.isValid();

        //then
        assertFalse(isOldTokenValid);
        assertEquals(refreshTokenText, token.getTokenText());
        assertTrue(isNewTokenValid);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("refresh()를 할 때, 주어진 text가 null 혹은 빈 문자열이면 예외가 발생한다.")
    void refresh_nullAndEmptyTokenText(String refreshTokenText) {
        //given
        final Token token = Token.createBearerToken("tokenText");

        //when, then
        assertThatThrownBy(() -> {
            token.refresh(refreshTokenText);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토큰은 null 혹은 빈 문자열이 될 수 없습니다.");
    }
}
