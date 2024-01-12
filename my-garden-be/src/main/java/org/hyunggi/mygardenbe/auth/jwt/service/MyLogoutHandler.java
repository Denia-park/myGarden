package org.hyunggi.mygardenbe.auth.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;
import org.hyunggi.mygardenbe.auth.jwt.entity.TokenEntity;
import org.hyunggi.mygardenbe.auth.jwt.repository.TokenRepository;
import org.hyunggi.mygardenbe.auth.jwt.util.JwtAuthUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessTokenText = JwtAuthUtil.extractTokenTextFromRequestHeader(request, TokenType.BEARER);

        if (accessTokenText == null) {
            return;
        }

        final TokenEntity storedToken = tokenRepository.findByTokenText(accessTokenText).orElse(null);

        if (storedToken != null) {
            storedToken.revoke();
            tokenRepository.save(storedToken);
        }
    }
}
