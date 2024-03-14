package org.hyunggi.mygardenbe.auth.jwt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.auth.jwt.domain.Token;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;
import org.springframework.util.Assert;

/**
 * 토큰 Entity
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TokenEntity extends BaseEntity {
    /**
     * 토큰 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 토큰 값
     */
    @Getter
    @Column(nullable = false, unique = true)
    private String tokenText;

    /**
     * 토큰 타입
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TokenType tokenType;

    /**
     * 폐기 여부
     */
    private boolean revoked;

    /**
     * 만료 여부
     */
    private boolean expired;

    /**
     * 회원 ID
     */
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

    /**
     * 토큰 생성
     *
     * @param token    토큰 Entity로 변환할 토큰 Domain
     * @param memberId 회원 ID
     * @return 토큰 Entity
     */
    public static TokenEntity of(final Token token, final Long memberId) {
        Assert.notNull(token, "토큰은 null이 될 수 없습니다.");
        Assert.isTrue(memberId != null && memberId > 0, "회원 ID는 null이 될 수 없고, 0보다 커야 합니다.");

        return TokenEntity.builder()
                .tokenText(token.getTokenText())
                .tokenType(token.getTokenType())
                .revoked(token.isRevoked())
                .expired(token.isExpired())
                .memberId(memberId)
                .build();
    }

    /**
     * 토큰 Entity를 토큰 Domain으로 변환
     *
     * @return 토큰 Domain
     */
    public Token toDomain() {
        return Token.of(this.tokenText, this.tokenType, this.revoked, this.expired);
    }

    /**
     * 토큰 업데이트
     *
     * @param token 업데이트할 토큰
     */
    public void update(final Token token) {
        Assert.notNull(token, "토큰은 null이 될 수 없습니다.");

        this.tokenText = token.getTokenText();
        this.tokenType = token.getTokenType();
        this.revoked = token.isRevoked();
        this.expired = token.isExpired();
    }
}
