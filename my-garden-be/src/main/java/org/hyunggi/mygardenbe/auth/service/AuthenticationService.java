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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    public Long signUp(final String email, final String password) {
        final String trimEmail = email.trim();
        final String trimPassword = password.trim();
        checkDuplicateEmail(trimEmail);

        final MemberEntity savedMember = saveMember(trimEmail, trimPassword);

        final String refreshToken = jwtService.generateRefreshToken(savedMember);

        saveToken(savedMember, refreshToken);

        return savedMember.getId();
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

    @Transactional
    public AuthenticationResponse login(final String email, final String password) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email.trim(), password.trim())
        );

        MemberEntity member = (MemberEntity) authenticate.getPrincipal();

        final TokenEntity tokenEntity = getTokenByEmail(member.getEmail());
        final Token token = tokenEntity.toDomain();

        String accessToken = jwtService.generateAccessToken(member);
        String refreshToken = jwtService.generateRefreshToken(member);

        token.refresh(refreshToken);
        tokenEntity.update(token);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private TokenEntity getTokenByEmail(final String email) {
        return tokenRepository.findTokenByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저의 토큰을 찾을 수 없습니다."));
    }
}
