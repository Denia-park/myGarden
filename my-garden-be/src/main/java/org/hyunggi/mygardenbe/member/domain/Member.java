package org.hyunggi.mygardenbe.member.domain;

import lombok.Getter;
import org.hyunggi.mygardenbe.common.exception.BusinessException;

@Getter
public class Member {
    private final String email;
    private final String password;
    private final Role role;
    private final boolean enabled;

    public Member(String email, String password) {
        validateConstructor(email, password);

        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.enabled = true;
    }

    private void validateConstructor(String email, String password) {
        validateEmail(email);

        validatePassword(password);
    }

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
}
