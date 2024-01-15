package org.hyunggi.mygardenbe.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.auth.controller.request.LoginRequest;
import org.hyunggi.mygardenbe.auth.controller.request.SignupRequest;
import org.hyunggi.mygardenbe.auth.service.AuthenticationService;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthenticationController.AUTH_BASE_API_PATH)
public class AuthenticationController {
    public static final String AUTH_BASE_API_PATH = "/api/auth";

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ApiResponse<Long> signUp(@RequestBody @Valid SignupRequest request) {
        final Long memberId = authenticationService.signUp(request.email(), request.password());

        return ApiResponse.ok(memberId);
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
        final AuthenticationResponse response = authenticationService.login(request.email(), request.password());

        return ApiResponse.ok(response);
    }
}
