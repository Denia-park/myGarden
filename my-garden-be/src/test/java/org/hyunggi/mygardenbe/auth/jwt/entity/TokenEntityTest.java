package org.hyunggi.mygardenbe.auth.jwt.entity;

import org.hyunggi.mygardenbe.auth.jwt.domain.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenEntityTest {

    @Test
    @DisplayName("정적 메서드 of의 입력으로 token과 memberId를 사용하여, 객체를 생성할 수 있다.")
    void of() {
        //given
        final Token token = Token.createBearerToken("tokenText");
        final Long memberId = 1L;

        //when
        final TokenEntity tokenEntity = TokenEntity.of(token, memberId);

        //then
        assertThat(tokenEntity).isNotNull();
        assertEquals(token.getTokenText(), tokenEntity.getTokenText());
        assertEquals(memberId, tokenEntity.getMemberId());
    }

    @Test
    @DisplayName("정적 메서드 of의 입력으로 token이 null이면 예외가 발생한다.")
    void of_nullToken() {
        //given
        final Token token = null;
        final Long memberId = 1L;

        //when, when
        assertThatThrownBy(() -> {
            TokenEntity.of(token, memberId);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토큰은 null이 될 수 없습니다.");
    }

    @ParameterizedTest
    @NullSource
    @CsvSource(value = {"0", "-1"})
    @DisplayName("정적 메서드 of의 입력으로 memberId가 null 혹은 0보다 작은 수면 예외가 발생한다.")
    void of_nullOrNegativeMemberId(final Long memberId) {
        //given
        final Token token = Token.createBearerToken("tokenText");

        //when, when
        assertThatThrownBy(() -> {
            TokenEntity.of(token, memberId);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("회원 ID는 null이 될 수 없고, 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("toDomain 메서드를 통해 Token 객체로 변환할 수 있다.")
    void toDomain() {
        //given
        final Token token = Token.createBearerToken("tokenText");
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

    @Test
    @DisplayName("update 메서드를 통해 Token 객체를 업데이트할 수 있다.")
    void update() {
        //given
        final Token token = Token.createBearerToken("tokenText");
        final Long memberId = 1L;
        final TokenEntity tokenEntity = TokenEntity.of(token, memberId);

        final Token newToken = Token.createBearerToken("newTokenText");

        //when
        tokenEntity.update(newToken);

        //then
        assertThat(tokenEntity.getTokenText()).isEqualTo(newToken.getTokenText());
    }
}
