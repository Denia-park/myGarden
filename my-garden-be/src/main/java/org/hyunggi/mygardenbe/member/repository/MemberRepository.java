package org.hyunggi.mygardenbe.member.repository;

import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 유저 Entity Repository
 */
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    /**
     * 유저 Entity를 이메일로 조회
     *
     * @param email 이메일
     * @return 유저 Entity
     */
    Optional<MemberEntity> findByEmail(final String email);
}
