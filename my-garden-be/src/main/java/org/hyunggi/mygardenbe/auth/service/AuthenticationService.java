package org.hyunggi.mygardenbe.auth.service;

import jakarta.persistence.EntityNotFoundException;
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

/**
 * 인증 Service
 */
@RequiredArgsConstructor
@Service
public class AuthenticationService {
    /**
     * 회원 Entity Repository
     */
    private final MemberRepository memberRepository;
    /**
     * 비밀번호 암호화시에 사용할 Encoder
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * JWT Service
     */
    private final JwtService jwtService;
    /**
     * 토큰 Entity Repository
     */
    private final TokenRepository tokenRepository;
    /**
     * Spring Security Authentication Manager
     */
    private final AuthenticationManager authenticationManager;

    /**
     * 회원 가입
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return 회원 ID
     */
    public Long signUp(final String email, final String password) {
        final String trimEmail = email.trim();
        final String trimPassword = password.trim();
        checkDuplicateEmail(trimEmail);

        final MemberEntity savedMember = saveMember(trimEmail, trimPassword);

        final String refreshToken = jwtService.generateRefreshToken(savedMember);

        saveToken(savedMember, refreshToken);

        return savedMember.getId();
    }

    /**
     * 이메일 중복 체크
     *
     * @param email 이메일
     */
    private void checkDuplicateEmail(final String email) {
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                });
    }

    /**
     * 회원 저장
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return 저장된 회원 Entity
     */
    private MemberEntity saveMember(final String email, final String password) {
        final MemberEntity member = MemberEntity.of(
                new Member(email, password),
                passwordEncoder
        );

        return memberRepository.save(member);
    }

    /**
     * 토큰 저장
     *
     * @param member   회원 Entity
     * @param jwtToken JWT 토큰
     */
    private void saveToken(final MemberEntity member, final String jwtToken) {
        final Token token = Token.createBearerToken(jwtToken);

        tokenRepository.save(TokenEntity.of(token, member.getId()));
    }

    /**
     * 로그인
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return 인증 응답
     */
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

    /**
     * 이메일로 토큰 Entity 조회
     *
     * @param email 이메일
     * @return 토큰 Entity
     */
    private TokenEntity getTokenByEmail(final String email) {
        return tokenRepository.findTokenByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저의 토큰을 찾을 수 없습니다."));
    }

    /**
     * 토큰 갱신
     * <br><br>
     * - Refresh Token을 통해 AccessToken과 RefreshToken을 갱신한다.
     *
     * @param refreshToken 리프레시 토큰
     * @return 갱신된 AccessToken과 RefreshToken을 담은 인증 응답
     */
    @Transactional
    public AuthenticationResponse refresh(final String refreshToken) {
        final TokenEntity tokenEntity = getRefreshTokenByTokenText(refreshToken);
        final Token token = tokenEntity.toDomain();

        final MemberEntity member = getMemberById(tokenEntity.getMemberId());

        final String newAccessToken = jwtService.generateAccessToken(member);
        final String newRefreshToken = jwtService.generateRefreshToken(member);

        token.refresh(newRefreshToken);
        tokenEntity.update(token);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * Refresh 토큰으로 토큰 Entity 조회
     *
     * @param refreshToken 리프레시 토큰
     * @return 토큰 Entity
     */
    private TokenEntity getRefreshTokenByTokenText(final String refreshToken) {
        return tokenRepository.findByTokenText(refreshToken)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리프레시 토큰입니다."));
    }

    /**
     * 회원 ID로 회원 Entity 조회
     *
     * @param memberId 회원 ID
     * @return 회원 Entity
     */
    private MemberEntity getMemberById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    }
}
