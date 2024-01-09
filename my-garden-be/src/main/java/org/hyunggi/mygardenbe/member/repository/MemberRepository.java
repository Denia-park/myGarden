package org.hyunggi.mygardenbe.member.repository;

import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmailAndPassword(String email, String password);

    Optional<MemberEntity> findByEmail(String email);
}
