package org.hyunggi.mygardenbe.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hyunggi.mygardenbe.common.exception.InvalidTokenRequestException;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 예외 처리 필터
 */
@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    /**
     * JWT 예외 처리 필터<br>
     * <br>
     * - InvalidTokenRequestException 예외가 발생하면, 예외 메시지를 응답으로 반환한다.
     *
     * @param request     Http 요청 객체
     * @param response    Http 응답 객체
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException      IO 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (InvalidTokenRequestException e) {
            setErrorResponse(response, e.getMessage());
        }
    }

    /**
     * JWT 예외 메세지를 응답으로 반환한다.
     *
     * @param response Http 응답 객체
     * @param message  응답 메시지
     */
    private void setErrorResponse(HttpServletResponse response, final String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Object> apiResponse = ApiResponse.of(HttpStatus.UNAUTHORIZED, message);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        } catch (IOException e) {
            final String jwtExceptionHandlerFilter = "JwtExceptionHandlerFilter error";

            log.warn(jwtExceptionHandlerFilter, e);
            throw new IllegalArgumentException(jwtExceptionHandlerFilter);
        }
    }
}
