package org.hyunggi.mygardenbe.auth.jwt.util;

import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthUtilTest {

    @Test
    @DisplayName("Request Header에서 Token Text 추출한다")
    void extractTokenTextFromRequestHeader() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testTokenValue");

        //when
        String tokenText = JwtAuthUtil.extractTokenTextFromRequestHeader(request, TokenType.BEARER);

        //then
        assertThat(tokenText).isEqualTo("testTokenValue");
    }

    @Test
    @DisplayName("Request Header에 Authorization이 없으면 null을 반환한다")
    void extractTokenTextFromRequestHeaderWhenNotAuthHeader() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        String tokenText = JwtAuthUtil.extractTokenTextFromRequestHeader(request, TokenType.BEARER);

        //then
        assertThat(tokenText).isNull();
    }

    @Test
    @DisplayName("Request Header에 Authorization이 있지만 Bearer Token이 아니면 null을 반환한다")
    void extractTokenTextFromRequestHeaderWhenAuthHeaderHasNotBearerToken() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bear testTokenValue");

        //when
        String tokenText = JwtAuthUtil.extractTokenTextFromRequestHeader(request, TokenType.BEARER);

        //then
        assertThat(tokenText).isNull();
    }
}
