package org.hyunggi.mygardenbe.member.controller;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.hyunggi.mygardenbe.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/api/auth/sign-up")
    public void signUp() {

    }
}
