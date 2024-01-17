package org.hyunggi.mygardenbe.member.entity;

import org.hyunggi.mygardenbe.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class MemberEntityTest {

    @Test
    @DisplayName("of 메서드의 입력으로 Member를 전달하여, MemberEntity객체를 생성할 수 있다.")
    void of() {
        //given
        Member member = new Member("test@test.com", "test1234!");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //when
        MemberEntity memberEntity = MemberEntity.of(member, passwordEncoder);

        System.out.println("memberEntity = " + memberEntity.getPassword());

        //then
        assertThat(memberEntity).isNotNull();
        assertThat(memberEntity.getEmail()).isEqualTo(member.getEmail());
        assertThat(passwordEncoder.matches(member.getPassword(), memberEntity.getPassword())).isTrue();
        assertThat(memberEntity.getRole()).isEqualTo(member.getRole());
        assertThat(memberEntity.isEnabled()).isEqualTo(member.isEnabled());
    }
}
