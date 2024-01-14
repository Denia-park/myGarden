package org.hyunggi.mygardenbe.auth.jwt.entity;

import org.hyunggi.mygardenbe.auth.jwt.domain.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenEntityTest {

    @Test
    @DisplayName("정적 메서드 of의 입력으로 token과 memberId를 사용하여, 객체를 생성할 수 있다.")
    void of() {
        //given
        Token token = Token.createBearerToken("tokenText");
        final Long memberId = 1L;

        //when
        final TokenEntity tokenEntity = TokenEntity.of(token, memberId);

        //then
        assertThat(tokenEntity).isNotNull();
        assertEquals(token.getTokenText(), tokenEntity.getTokenText());
        assertEquals(memberId, tokenEntity.getMemberId());
    }

    @Test
    @DisplayName("toDomain 메서드를 통해 Token 객체로 변환할 수 있다.")
    void toDomain() {
        //given
        Token token = Token.createBearerToken("tokenText");
        final Long memberId = 1L;
        final TokenEntity tokenEntity = TokenEntity.of(token, memberId);

        //when
        final Token tokenFromEntity = tokenEntity.toDomain();

        //then
        assertThat(tokenFromEntity).isNotNull();
        assertEquals(token.getTokenText(), tokenFromEntity.getTokenText());
        assertEquals(token.getTokenType(), tokenFromEntity.getTokenType());
        assertEquals(token.isRevoked(), tokenFromEntity.isRevoked());
        assertEquals(token.isExpired(), tokenFromEntity.isExpired());
    }
}
