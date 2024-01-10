package org.hyunggi.mygardenbe.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.auth.controller.request.SignupRequest;
import org.hyunggi.mygardenbe.auth.controller.response.AuthenticationResponse;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthController.AUTH_API_PATH)
public class AuthController {
    public static final String AUTH_API_PATH = "/api/auth";

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ApiResponse<AuthenticationResponse> signUp(@RequestBody @Valid SignupRequest request) {
        final AuthenticationResponse response = memberService.signUp(request.email(), request.password());

        return ApiResponse.ok(response);
    }
}
