package org.hyunggi.mygardenbe.auth.jwt.util;

import jakarta.servlet.http.HttpServletRequest;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;

public class JwtAuthUtil {
    public static final String AUTH_HEADER = "Authorization";

    private JwtAuthUtil() {
        //Utility class
    }

    public static String extractTokenTextFromRequestHeader(final HttpServletRequest request, final TokenType tokenType) {
        if (hasNotAuthHeader(request)) {
            return null;
        }

        String authHeader = request.getHeader(AUTH_HEADER);

        return authHeader.substring(tokenType.getParseTextLength());
    }

    private static boolean hasNotAuthHeader(final HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);

        return authHeader == null || !authHeader.startsWith(TokenType.BEARER.getParseText());
    }
}
