package org.hyunggi.mygardenbe.auth.jwt.repository;

import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.auth.jwt.domain.Token;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;
import org.hyunggi.mygardenbe.auth.jwt.entity.TokenEntity;
import org.hyunggi.mygardenbe.member.domain.Member;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class TokenRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("UserEmail로 Token을 찾는다.")
    void findTokenByUserEmail() {
        //given
        final String email = "test@test.com";
        final MemberEntity savedMember = memberRepository.save(makeMember(email, "test1234!"));
        final Long memberId = savedMember.getId();

        final String tokenText = "test";
        tokenRepository.save(makeToken(memberId, tokenText));

        //when
        final TokenEntity token = tokenRepository.findTokenByUserEmail(email).get();

        //then
        assertThat(token).isNotNull();
        assertThat(token)
                .extracting("tokenText", "tokenType", "revoked", "expired", "memberId")
                .containsExactly(tokenText, TokenType.BEARER, false, false, memberId);
    }

    private MemberEntity makeMember(final String email, final String password) {
        final Member member = new Member(email, password);

        return MemberEntity.of(member, passwordEncoder);
    }

    private TokenEntity makeToken(final Long memberId, final String tokenText) {
        final Token token = Token.createBearerToken(tokenText);

        return TokenEntity.of(token, memberId);
    }
}
