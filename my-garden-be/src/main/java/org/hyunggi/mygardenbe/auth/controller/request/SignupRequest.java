package org.hyunggi.mygardenbe.auth.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SignupRequest(
        @NotBlank(message = "이메일은 null이 될 수 없습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotBlank(message = "비밀번호는 null이 될 수 없습니다.")
        String password
) {
}
