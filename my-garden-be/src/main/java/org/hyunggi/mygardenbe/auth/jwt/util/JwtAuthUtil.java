package org.hyunggi.mygardenbe.auth.jwt.util;

import jakarta.servlet.http.HttpServletRequest;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;

/**
 * JWT 인증 관련 유틸 클래스
 */
public class JwtAuthUtil {
    /**
     * Token이 전달되는 Http Header
     */
    public static final String AUTH_HEADER = "Authorization";

    private JwtAuthUtil() {
        //Utility class
    }

    /**
     * Request Header에서 Token 값 추출
     *
     * @param request   Http 요청
     * @param tokenType Token Type
     * @return Token 값
     */
    public static String extractTokenTextFromRequestHeader(final HttpServletRequest request, final TokenType tokenType) {
        if (hasNotAuthHeader(request)) {
            return null;
        }

        String authHeader = request.getHeader(AUTH_HEADER);

        return authHeader.substring(tokenType.getParseTextLength());
    }

    /**
     * Request Header에 Authorization Header가 없는지 확인
     *
     * @param request Http 요청
     * @return Authorization Header가 없으면 true, 있으면 false
     */
    private static boolean hasNotAuthHeader(final HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);

        return authHeader == null || !authHeader.startsWith(TokenType.BEARER.getParseText());
    }
}
