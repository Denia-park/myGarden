package org.hyunggi.mygardenbe.auth.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

/**
 * 로그인 요청시에 사용하는 Request DTO
 *
 * @param email    이메일
 * @param password 비밀번호
 */
@Builder
public record LoginRequest(
        @NotEmpty(message = "이메일은 null이 될 수 없습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotEmpty(message = "비밀번호는 null이 될 수 없습니다.")
        String password
) {
}
