package org.hyunggi.mygardenbe.auth.jwt.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTypeTest {
    @Test
    @DisplayName("해당 TokenType의 parseText 길이를 반환한다.")
    void getParseTextLength() {
        // given
        TokenType tokenType = TokenType.BEARER;

        // when
        int parseTextLength = tokenType.getParseTextLength();

        // then
        assertThat(parseTextLength).isEqualTo("Bearer ".length());
    }
}
