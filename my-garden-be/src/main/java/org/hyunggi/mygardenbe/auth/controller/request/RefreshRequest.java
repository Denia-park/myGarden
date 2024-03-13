package org.hyunggi.mygardenbe.auth.controller.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 토큰 재발급 요청시에 사용하는 Request DTO [로그인시에 발급했던 리프레시 토큰을 사용하여 재발급]
 *
 * @param refreshToken 리프레시 토큰
 */
public record RefreshRequest(
        @NotBlank(message = "리프레시 토큰은 비어있을 수 없습니다.")
        String refreshToken
) {
}
