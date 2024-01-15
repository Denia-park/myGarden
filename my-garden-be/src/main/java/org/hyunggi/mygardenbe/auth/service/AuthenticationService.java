package org.hyunggi.mygardenbe.auth.service;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.auth.jwt.domain.Token;
import org.hyunggi.mygardenbe.auth.jwt.entity.TokenEntity;
import org.hyunggi.mygardenbe.auth.jwt.repository.TokenRepository;
import org.hyunggi.mygardenbe.auth.jwt.service.JwtService;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.hyunggi.mygardenbe.member.domain.Member;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse signUp(final String email, final String password) {
        final String trimEmail = email.trim();
        final String trimPassword = password.trim();
        checkDuplicateEmail(trimEmail);

        final MemberEntity savedMember = saveMember(trimEmail, trimPassword);

        final String accessToken = jwtService.generateAccessToken(savedMember);
        final String refreshToken = jwtService.generateRefreshToken(savedMember);

        saveToken(savedMember, accessToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void checkDuplicateEmail(final String email) {
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                });
    }

    private MemberEntity saveMember(final String email, final String password) {
        final MemberEntity member = MemberEntity.of(
                new Member(email, password),
                passwordEncoder
        );

        return memberRepository.save(member);
    }

    private void saveToken(final MemberEntity member, final String jwtToken) {
        final Token token = Token.createBearerToken(jwtToken);

        tokenRepository.save(TokenEntity.of(token, member.getId()));
    }
}
