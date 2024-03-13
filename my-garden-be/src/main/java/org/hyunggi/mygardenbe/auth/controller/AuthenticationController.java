package org.hyunggi.mygardenbe.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.auth.controller.request.LoginRequest;
import org.hyunggi.mygardenbe.auth.controller.request.RefreshRequest;
import org.hyunggi.mygardenbe.auth.controller.request.SignupRequest;
import org.hyunggi.mygardenbe.auth.service.AuthenticationService;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AuthenticationController.AUTH_BASE_API_PATH)
public class AuthenticationController {
    /**
     * 인증 API의 기본 경로
     */
    public static final String AUTH_BASE_API_PATH = "/api/auth";

    /**
     * 인증 서비스
     */
    private final AuthenticationService authenticationService;

    /**
     * 회원가입
     *
     * @param request 회원가입 요청 객체
     * @return 회원가입의 결과로 생성된 회원 ID를 반환
     */
    @PostMapping("/signup")
    public ApiResponse<Long> signUp(@RequestBody @Valid SignupRequest request) {
        final Long memberId = authenticationService.signUp(request.email(), request.password());

        return ApiResponse.ok(memberId);
    }

    /**
     * 로그인
     *
     * @param request 로그인 요청 객체
     * @return 로그인의 결과로 생성된 토큰들을 반환 (accessToken, refreshToken)
     */
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
        final AuthenticationResponse response = authenticationService.login(request.email(), request.password());

        return ApiResponse.ok(response);
    }

    /**
     * 토큰 재발급
     *
     * @param request 토큰 재발급 요청 객체
     * @return 토큰 재발급의 결과로 생성된 토큰들을 반환 (accessToken, refreshToken)
     */
    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        final AuthenticationResponse response = authenticationService.refresh(request.refreshToken());

        return ApiResponse.ok(response);
    }
}
