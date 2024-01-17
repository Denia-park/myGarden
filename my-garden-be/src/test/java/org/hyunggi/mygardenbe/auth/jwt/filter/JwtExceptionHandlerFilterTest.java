package org.hyunggi.mygardenbe.auth.jwt.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.hyunggi.mygardenbe.common.exception.InvalidTokenRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class JwtExceptionHandlerFilterTest {
    @Test
    @DisplayName("해당 filter 이후에 InvalidTokenRequestException이 발생하면 Http 상태코드로 401 Unauthorized를 반환한다")
    void doFilterInternal() throws ServletException, IOException {
        //given
        JwtExceptionHandlerFilter jwtExceptionHandlerFilter = new JwtExceptionHandlerFilter();
        Filter throwExceptionFilter = buildThrowExceptionFilter();

        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        final MockFilterChain mockFilterChain = new MockFilterChain(buildMockHttpServlet(), throwExceptionFilter);

        //when
        jwtExceptionHandlerFilter.doFilterInternal(request, mockHttpServletResponse, mockFilterChain);

        //then
        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(mockHttpServletResponse.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(mockHttpServletResponse.getContentAsString())
                .isEqualTo("{\"code\":401,\"status\":\"UNAUTHORIZED\",\"message\":\"UNAUTHORIZED\",\"data\":\"test\"}");
    }

    private Filter buildThrowExceptionFilter() {
        return (request, response, chain) -> {
            throw new InvalidTokenRequestException("test");
        };
    }

    private Servlet buildMockHttpServlet() {
        return new HttpServlet() {
        };
    }
}
