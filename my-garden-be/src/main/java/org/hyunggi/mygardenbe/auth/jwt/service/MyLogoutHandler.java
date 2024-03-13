package org.hyunggi.mygardenbe.auth.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.auth.jwt.domain.Token;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;
import org.hyunggi.mygardenbe.auth.jwt.entity.TokenEntity;
import org.hyunggi.mygardenbe.auth.jwt.repository.TokenRepository;
import org.hyunggi.mygardenbe.auth.jwt.util.JwtAuthUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * Logout 핸들러 (Logout 요청이 들어왔을 때 사용)
 */
@RequiredArgsConstructor
@Service
public class MyLogoutHandler implements LogoutHandler {
    /**
     * 토큰 Entity Repository
     */
    private final TokenRepository tokenRepository;

    /**
     * Logout 요청시 실행되는 메서드
     * <br><br>
     * - Logout 요청이 오면, 같이 전달된 accessToken을 통해 저장된 RefreshToken을 폐기한다.
     *
     * @param request        Http 요청
     * @param response       Http 응답
     * @param authentication Spring Security Authentication Interface
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessTokenText = JwtAuthUtil.extractTokenTextFromRequestHeader(request, TokenType.BEARER);

        if (accessTokenText == null) {
            return;
        }

        final TokenEntity storedToken = tokenRepository.findByTokenText(accessTokenText).orElse(null);

        if (storedToken != null) {
            final Token token = storedToken.toDomain();
            token.revoke();
            storedToken.update(token);
            tokenRepository.save(storedToken);
        }
    }
}
