package org.hyunggi.mygardenbe.member.repository;

import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByEmailAndPassword(String email, String password);
}
