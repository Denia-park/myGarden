package org.hyunggi.mygardenbe.member.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.common.jwt.JwtService;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.hyunggi.mygardenbe.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @PostMapping("/api/auth/sign-up")
    public void signUp() {

    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<Long>> signIn(@RequestBody Map<String, String> paramas, HttpServletResponse response) {
        final MemberEntity member = memberRepository.findByEmailAndPassword(
                paramas.get("email"),
                paramas.get("password")
        ).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        addJwtToResponseCookie(response, member);

        return ResponseEntity.ok(ApiResponse.ok(member.getId()));
    }

    private void addJwtToResponseCookie(final HttpServletResponse response, final MemberEntity member) {
        final String token = jwtService.getToken("id", member.getId());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }
}
