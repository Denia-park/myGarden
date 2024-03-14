package org.hyunggi.mygardenbe.member.domain;

import lombok.Getter;
import org.hyunggi.mygardenbe.common.exception.BusinessException;

/**
 * 유저 Domain
 */
@Getter
public class Member {
    /**
     * 유저 이메일
     */
    private final String email;

    /**
     * 유저 비밀번호
     */
    private final String password;

    /**
     * 유저 권한
     */
    private final Role role;

    /**
     * 유저 활성화 여부
     */
    private final boolean enabled;

    public Member(final String email, final String password) {
        this(email, password, Role.USER, true);
    }

    public Member(final String email, final String password, final Role role, final boolean enabled) {
        validateConstructor(email, password, role);

        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    /**
     * 유저 생성 인자 유효성 검증
     *
     * @param email    이메일
     * @param password 비밀번호
     * @param role     권한
     */
    private void validateConstructor(final String email, final String password, final Role role) {
        validateEmail(email);
        validatePassword(password);
        validateRole(role);
    }

    /**
     * 이메일 유효성 검증
     *
     * @param email 이메일
     */
    private void validateEmail(final String email) {
        if (email == null || email.isEmpty()) {
            throw new BusinessException("이메일은 null이거나 비어있을 수 없습니다.");
        }

        /*
        EMAIL RULE
            - 기존에 흔히 사용하는 이메일 형식
         */
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!email.matches(emailRegex)) {
            throw new BusinessException("이메일 형식에 맞지 않습니다.");
        }
    }

    /**
     * 비밀번호 유효성 검증
     *
     * @param password 비밀번호
     */
    private void validatePassword(final String password) {
        if (password == null || password.isEmpty()) {
            throw new BusinessException("비밀번호는 null이거나 비어있을 수 없습니다.");
        }

        /*
        PASSWORD RULES
            - 반드시 영문자가 1개 이상 포함되어야 합니다.
            - 숫자가 1개 이상 포함되어야 합니다.
            - 지정된 세트의 특수 문자를 하나 이상 포함해야 합니다. ($, @, !, %, *, #, ?, &)
            - 길이는 8~20자 사이여야 합니다.
         */
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&])[A-Za-z\\d$@!%*#?&]{8,20}$";
        if (!password.matches(passwordRegex)) {
            throw new BusinessException("비밀번호는 8자 이상 20자 이하여야 하며, 영문자, 숫자, 특수문자를 각각 1개 이상씩 포함해야 합니다.");
        }
    }

    /**
     * 권한 유효성 검증
     *
     * @param role 권한
     */
    private void validateRole(final Role role) {
        if (role == null) {
            throw new BusinessException("Role은 null이 될 수 없습니다.");
        }
    }
}
