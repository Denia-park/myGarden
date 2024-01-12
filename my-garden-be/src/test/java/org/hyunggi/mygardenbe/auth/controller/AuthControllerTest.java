package org.hyunggi.mygardenbe.auth.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.auth.controller.request.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {
    @Test
    @DisplayName("회원가입을 한다.")
    void signUp() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password("test1234!")
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        BDDMockito.verify(memberService).signUp(request.email(), request.password());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("회원가입을 할 때 이메일이 null 혹은 비어있으면, 400을 반환한다.")
    void signUpWithNullEmail(String email) throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .email(email)
                .password("test1234!")
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입을 할 때 이메일이 이메일 형식이 아니면 400을 반환한다.")
    void signUpWithInvalidEmail() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .email("test")
                .password("test1234!")
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("회원가입을 할 때 비밀번호가 null 혹은 비어있으면 400을 반환한다.")
    void signUpWithNullPassword(String password) throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password(password)
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }
}
