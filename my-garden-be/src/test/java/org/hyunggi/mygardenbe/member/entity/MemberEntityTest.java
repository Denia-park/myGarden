package org.hyunggi.mygardenbe.member.entity;

import org.hyunggi.mygardenbe.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("of 메서드의 입력으로 null을 전달하면, IllegalArgumentException이 발생한다.")
    void of_null() {
        //given
        Member member = null;
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //when, then
        assertThatThrownBy(() -> MemberEntity.of(member, passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Member는 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("of 메서드의 입력으로 PasswordEncoder를 전달하면, IllegalArgumentException이 발생한다.")
    void of_null_passwordEncoder() {
        //given
        Member member = new Member("test@test.com", "test1234!");
        PasswordEncoder passwordEncoder = null;

        //when, then
        assertThatThrownBy(() -> MemberEntity.of(member, passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PasswordEncoder는 null이 될 수 없습니다.");
    }
}
