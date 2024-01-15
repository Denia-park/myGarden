package org.hyunggi.mygardenbe.auth.service;

import org.assertj.core.groups.Tuple;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;
import org.hyunggi.mygardenbe.auth.jwt.entity.TokenEntity;
import org.hyunggi.mygardenbe.auth.jwt.repository.TokenRepository;
import org.hyunggi.mygardenbe.member.domain.Role;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @DisplayName("회원 가입을 하면, Member와 refreshToken을 데이터베이스에 저장하고 Member의 ID를 반환한다. ")
    void signUp() {
        //given
        String email = "test@test.com";
        String password = "test1234!";

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
}
