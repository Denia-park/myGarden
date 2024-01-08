package org.hyunggi.mygardenbe.member.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.hyunggi.mygardenbe.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    private final MemberRepository memberRepository;


    @PostMapping("/api/auth/sign-up")
    public void signUp() {

    }

    @PostMapping("/api/auth/login")
    public ApiResponse<Long> signIn(@RequestBody Map<String, String> paramas) {
        final MemberEntity member = memberRepository.findByEmailAndPassword(
                paramas.get("email"),
                paramas.get("password")
        ).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        return ApiResponse.ok(member.getId());
    }
}
