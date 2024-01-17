package org.hyunggi.mygardenbe.auth.jwt.service;

import io.jsonwebtoken.Claims;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.auth.service.AuthenticationService;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class JwtServiceTest extends IntegrationTestSupport {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Token에서 정보를 빼내어, Claims 객체를 만들어 반환한다")
    void extractAllClaims() {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";
        final Long memberId = authenticationService.signUp(email, password);
        final AuthenticationResponse response = authenticationService.login(email, password);
        final String accessToken = response.accessToken();

        //when
        final Claims claims = jwtService.extractAllClaims(accessToken);

        //then
        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(email);
        assertThat(claims.get("roles", String.class)).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("UserDetails를 넣으면, AccessToken을 생성하여 반환한다")
    void generateAccessToken() {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";
        final Long memberId = authenticationService.signUp(email, password);
        final MemberEntity memberEntity = memberRepository.findById(memberId).get();

        //when
        final String accessToken = jwtService.generateAccessToken(memberEntity);

        //then
        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("UserDetails를 넣으면, RefreshToken을 생성하여 반환한다")
    void generateRefreshToken() {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";
        final Long memberId = authenticationService.signUp(email, password);
        final MemberEntity memberEntity = memberRepository.findById(memberId).get();

        //when
        final String refreshToken = jwtService.generateRefreshToken(memberEntity);

        //then
        assertThat(refreshToken).isNotNull();
    }

    @Test
    @DisplayName("Token을 넣으면, Authorities를 반환한다")
    void convertStringToAuthorities() {
        //given
        final String rolesText = "ROLE_USER,ROLE_ADMIN,ROLE_MANAGER";

        //when
        final Collection<? extends GrantedAuthority> grantedAuthorities = jwtService.convertStringToAuthorities(rolesText);

        //then
        assertThat(grantedAuthorities).hasSize(3)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER");
    }
}
