package org.hyunggi.mygardenbe.auth.jwt.repository;

import org.hyunggi.mygardenbe.auth.jwt.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * 토큰 Entity Repository
 */
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    /**
     * 회원 ID로 토큰 Entity 조회
     *
     * @param memberId 회원 ID
     * @return 토큰 Entity
     */
    Optional<TokenEntity> findByMemberId(final Long memberId);

    /**
     * 토큰 값으로 토큰 Entity 조회
     *
     * @param tokenText 토큰 값
     * @return 토큰 Entity
     */
    Optional<TokenEntity> findByTokenText(final String tokenText);

    /**
     * 사용자 이메일로 토큰 Entity 조회
     *
     * @param userEmail 사용자 이메일
     * @return 토큰 Entity
     */
    @Query("select t from TokenEntity t join MemberEntity m on t.memberId = m.id where m.email = :userEmail")
    Optional<TokenEntity> findTokenByUserEmail(String userEmail);
}
