package org.hyunggi.mygardenbe.auth.controller;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.hyunggi.mygardenbe.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthController.AUTH_API_PATH)
public class AuthController {
    public static final String AUTH_API_PATH = "/api/auth";

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/sign-up")
    public void signUp() {

    }
}
