package org.hyunggi.mygardenbe.member.domain;

import org.hyunggi.mygardenbe.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    @Test
    @DisplayName("이메일과 비밀번호가 주어지면 Member 객체가 생성된다.")
    void createMember() {
        // given
        String email = "test@test.com";
        String password = "password1!";

        // when
        Member member = new Member(email, password);

        // then
        assertThat(member).isNotNull();
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이메일이 null이거나 비어있으면 예외가 발생한다.")
    void emailIsNullAndEmpty(String email) {
        // given
        String password = "password";

        // when, then
        assertThatThrownBy(() -> new Member(email, password))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("이메일은 null이거나 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "test@", "test@test", "test@test.", "test@test.c"})
    @DisplayName("이메일이 이메일 형식에 맞지 않으면 예외가 발생한다.")
    void emailIsInvalid(String email) {
        // given
        String password = "password";

        // when, then
        assertThatThrownBy(() -> new Member(email, password))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("이메일 형식에 맞지 않습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("비밀번호가 null이거나 비어있으면 예외가 발생한다.")
    void passwordIsNullAndEmpty(String password) {
        // given
        String email = "test@test.com";

        // when, then
        assertThatThrownBy(() -> new Member(email, password))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("비밀번호는 null이거나 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("비밀번호가 8자 이상 20자 이하가 아니면 예외가 발생한다.")
    @ValueSource(strings = {"1234567", "123456789012345678901"})
    void passwordIsInvalidLength(String password) {
        // given
        String email = "test@test.com";

        // when, then
        assertThatThrownBy(() -> new Member(email, password))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("비밀번호는 8자 이상 20자 이하여야 하며, 영문자, 숫자, 특수문자를 각각 1개 이상씩 포함해야 합니다.");
    }

    @ParameterizedTest
    @DisplayName("비밀번호가 영문자, 숫자, 특수문자를 각각 1개 이상씩 포함하지 않으면 예외가 발생한다.")
    @ValueSource(strings = {"12345678", "abcdefgh", "!@#$%^&*", "12345678a", "12345678!", "abcdefgh1", "abcdefgh!", "!@#$%^&*1", "!@#$%^&*a"})
    void passwordIsInvalid(String password) {
        // given
        String email = "test@test.com";

        // when, then
        assertThatThrownBy(() -> new Member(email, password))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("비밀번호는 8자 이상 20자 이하여야 하며, 영문자, 숫자, 특수문자를 각각 1개 이상씩 포함해야 합니다.");
    }
}
