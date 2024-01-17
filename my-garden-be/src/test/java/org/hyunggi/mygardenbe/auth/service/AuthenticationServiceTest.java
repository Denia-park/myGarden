package org.hyunggi.mygardenbe.auth.service;

import org.assertj.core.groups.Tuple;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.auth.controller.request.RefreshRequest;
import org.hyunggi.mygardenbe.auth.jwt.domain.Token;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;
import org.hyunggi.mygardenbe.auth.jwt.entity.TokenEntity;
import org.hyunggi.mygardenbe.auth.jwt.repository.TokenRepository;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.hyunggi.mygardenbe.member.domain.Role;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class AuthenticationServiceTest extends IntegrationTestSupport {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private LogoutHandler logoutHandler;

    @Test
    @DisplayName("회원 가입을 하면, Member와 refreshToken을 데이터베이스에 저장하고 Member의 ID를 반환한다. ")
    void signUp() {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";

        //when
        final Long memberId = authenticationService.signUp(email, password);

        //then
        //  Member 저장 확인
        final List<MemberEntity> members = memberRepository.findAll();
        final Long savedMemberId = members.get(0).getId();

        assertThat(members).hasSize(1)
                .allMatch(member -> passwordEncoder.matches(password, member.getPassword()))
                .extracting("id", "email", "role", "enabled")
                .containsExactly(
                        Tuple.tuple(savedMemberId, email, Role.USER, true)
                );

        //  Token 저장 확인
        final List<TokenEntity> tokens = tokenRepository.findAll();

        assertThat(tokens).hasSize(1)
                .extracting("tokenType", "memberId", "revoked", "expired")
                .containsExactly(
                        Tuple.tuple(TokenType.BEARER, savedMemberId, false, false)
                );

        //  반환된 Member ID 확인
        assertThat(memberId).isEqualTo(savedMemberId);
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원 가입을 하면, IllegalArgumentException을 던진다.")
    void signUpWithDuplicateEmail() {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";
        authenticationService.signUp(email, password);

        //when, then
        assertThatThrownBy(() -> authenticationService.signUp(email, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("로그인에 성공하면, accessToken과 refreshToken을 반환하며 토큰 데이터베이스에는 새로 생성된 refreshToken 정보로 업데이트된다.")
    void login() {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";

        final Long memberId = authenticationService.signUp(email, password);
        final TokenEntity oldToken = tokenRepository.findByMemberId(memberId).get();
        final String oldTokenText = changeTokenTextForCheckingUpdate(oldToken.getTokenText());

        //when
        final AuthenticationResponse authenticationResponse = authenticationService.login(email, password);

        //then
        //  반환된 Token 확인
        final String accessToken = authenticationResponse.accessToken();
        final String refreshToken = authenticationResponse.refreshToken();

        assertThat(accessToken).isNotNull();
        assertThat(refreshToken).isNotNull();

        //  Token이 업데이트 되었는지 확인
        final TokenEntity newToken = tokenRepository.findTokenByUserEmail(email).get();
        assertThat(newToken.getTokenText()).isNotEqualTo(oldTokenText);
        assertThat(newToken.getTokenText()).isEqualTo(refreshToken);
    }

    private String changeTokenTextForCheckingUpdate(final String tokenText) {
        //테스트를 위해 토큰 텍스트의 마지막 문자를 변경한다.
        //JWT에서 iss는 초 단위의 시간을 나타내므로, 문자 변경을 하지 않으면 로그인과의 시간차이가 1초 이하로 나타나서 테스트가 실패한다.

        return tokenText + " ";
    }

    @Test
    @DisplayName("로그아웃이 되면, 토큰 데이터베이스에 저장된 refreshToken의 revoked 및 expired가 true로 업데이트된다.")
    void logout() {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";

        final Long memberId = authenticationService.signUp(email, password);
        final AuthenticationResponse response = authenticationService.login(email, password);

        final TokenEntity oldRefreshToken = tokenRepository.findByMemberId(memberId).get();
        final Token oldToken = oldRefreshToken.toDomain();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + response.refreshToken());

        //when
        logoutHandler.logout(request, null, null);

        //then
        final TokenEntity newRefreshToken = tokenRepository.findByMemberId(memberId).get();
        final Token newToken = newRefreshToken.toDomain();

        assertThat(oldToken.isRevoked()).isFalse();
        assertThat(oldToken.isExpired()).isFalse();
        assertThat(newToken.isRevoked()).isTrue();
        assertThat(newToken.isExpired()).isTrue();
    }

    @Test
    @DisplayName("refresh 메서드가 호출 되면, 전달받은 refreshToken으로 accessToken 및 refreshToken을 재발급 받아서 반환하고," +
            " 토큰 데이터베이스에는 새로 생성된 refreshToken 정보로 업데이트된다.")
    void refresh() {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";

        final Long memberId = authenticationService.signUp(email, password);
        final AuthenticationResponse oldResponse = authenticationService.login(email, password);

        final TokenEntity oldRefreshToken = tokenRepository.findByMemberId(memberId).get();
        final String oldAccessTokenText = changeTokenTextForCheckingUpdate(oldResponse.accessToken());
        final String oldRefreshTokenText = changeTokenTextForCheckingUpdate(oldResponse.refreshToken());

        final RefreshRequest refreshRequest = new RefreshRequest(oldRefreshToken.getTokenText());

        //when
        final AuthenticationResponse newResponse = authenticationService.refresh(refreshRequest.refreshToken());

        //then
        //  반환된 Token 확인
        final String newAccessToken = newResponse.accessToken();
        final String newRefreshToken = newResponse.refreshToken();

        assertThat(newAccessToken).isNotNull();
        assertThat(newRefreshToken).isNotNull();

        //  Token이 업데이트 되었는지 확인
        final TokenEntity newRefreshTokenEntity = tokenRepository.findByTokenText(newRefreshToken).get();

        assertThat(newAccessToken).isNotEqualTo(oldAccessTokenText);
        assertThat(newRefreshToken).isNotEqualTo(oldRefreshTokenText);
        assertThat(newRefreshTokenEntity.getTokenText()).isEqualTo(newRefreshToken);
    }
}
