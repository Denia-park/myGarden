package org.hyunggi.mygardenbe.member.controller;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.member.service.MemberService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

}
