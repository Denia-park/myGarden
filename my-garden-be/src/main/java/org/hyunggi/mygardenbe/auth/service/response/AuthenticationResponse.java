package org.hyunggi.mygardenbe.auth.service.response;

import lombok.Builder;

/**
 * 로그인 성공시에 반환되는 응답
 *
 * @param accessToken  JWT Access 토큰
 * @param refreshToken JWT Refresh 토큰
 */
@Builder
public record AuthenticationResponse(String accessToken, String refreshToken) {
}
