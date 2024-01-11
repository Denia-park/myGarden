package org.hyunggi.mygardenbe.auth.controller.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String accessToken, String refreshToken) {
}
