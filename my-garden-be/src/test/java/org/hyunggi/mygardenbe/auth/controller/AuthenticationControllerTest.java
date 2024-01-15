package org.hyunggi.mygardenbe.auth.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.auth.controller.request.LoginRequest;
import org.hyunggi.mygardenbe.auth.controller.request.SignupRequest;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.hyunggi.mygardenbe.common.ApiTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest extends ControllerTestSupport {
    @Test
    @DisplayName("회원가입을 한다.")
    void signUp() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password("test1234!")
                .build();

        given(authenticationService.signUp(request.email(), request.password()))
                .willReturn(1L);

        //when, then
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1L));
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

    @Test
    @DisplayName("로그인을 한다.")
    void login() throws Exception {
        //given
        String email = "test@test.com";
        String password = "test1234!";

        ApiTestUtil.signUp(mockMvc, email, password);

        given(authenticationService.login(email, password))
                .willReturn(
                        AuthenticationResponse.builder()
                                .accessToken("accessToken")
                                .refreshToken("refreshToken")
                                .build()
                );

        //when, then
        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                LoginRequest.builder()
                                                        .email(email)
                                                        .password(password)
                                                        .build()
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("refreshToken"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("로그인을 할 때 이메일이 null 혹은 비어있으면 400을 반환한다.")
    void loginWithNullEmail(String email) throws Exception {
        //given
        String password = "test1234!";

        ApiTestUtil.signUp(mockMvc, "test@test.com", "test1234!");

        //when, then
        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                LoginRequest.builder()
                                                        .email(email)
                                                        .password(password)
                                                        .build()
                                        )
                                )
                )
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("로그인을 할 때 비밀번호가 null 혹은 비어있으면 400을 반환한다.")
    void loginWithNullPassword(String password) throws Exception {
        //given
        String email = "test@test.com";

        ApiTestUtil.signUp(mockMvc, "test@test.com", "test1234!");

        //when, then
        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                LoginRequest.builder()
                                                        .email(email)
                                                        .password(password)
                                                        .build()
                                        )
                                )
                )
                .andExpect(status().isBadRequest());
    }
}
