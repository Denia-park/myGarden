package org.hyunggi.mygardenbe.auth.jwt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.auth.jwt.domain.Token;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TokenEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Column(nullable = false, unique = true)
    private String tokenText;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;
    @Getter
    private Long memberId;

    @Builder(access = AccessLevel.PRIVATE)
    private TokenEntity(final String tokenText, final TokenType tokenType, final boolean revoked, final boolean expired, final Long memberId) {
        this.tokenText = tokenText;
        this.tokenType = tokenType;
        this.revoked = revoked;
        this.expired = expired;
        this.memberId = memberId;
    }

    public static TokenEntity of(final Token token, final Long memberId) {
        return TokenEntity.builder()
                .tokenText(token.getTokenText())
                .tokenType(token.getTokenType())
                .revoked(token.isRevoked())
                .expired(token.isExpired())
                .memberId(memberId)
                .build();
    }

    public Token toDomain() {
        return Token.of(this.tokenText, this.tokenType, this.revoked, this.expired);
    }
}
