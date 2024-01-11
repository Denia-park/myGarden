package org.hyunggi.mygardenbe.auth.jwt.repository;

import org.hyunggi.mygardenbe.auth.jwt.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByMemberId(final Long memberId);

    Optional<TokenEntity> findByTokenText(final String tokenText);

    @Query("select t from TokenEntity t join MemberEntity m on t.memberId = m.id where m.email = :userEmail")
    Optional<TokenEntity> findTokenByUserEmail(String userEmail);
}
