package org.hyunggi.mygardenbe.auth.jwt.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

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
}
