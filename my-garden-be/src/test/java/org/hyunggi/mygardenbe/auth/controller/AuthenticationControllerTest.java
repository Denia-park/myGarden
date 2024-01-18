package org.hyunggi.mygardenbe.auth.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.auth.controller.request.LoginRequest;
import org.hyunggi.mygardenbe.auth.controller.request.RefreshRequest;
import org.hyunggi.mygardenbe.auth.controller.request.SignupRequest;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest extends ControllerTestSupport {
    @Test
    @DisplayName("회원가입을 한다.")
    void signUp() throws Exception {
        //given
        final SignupRequest request = SignupRequest.builder()
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
    void signUpWithNullEmail(final String email) throws Exception {
        //given
        final SignupRequest request = SignupRequest.builder()
                .email(email)
                .password("test1234!")
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일은 null이 될 수 없습니다."));
    }

    @Test
    @DisplayName("회원가입을 할 때 이메일이 이메일 형식이 아니면 400을 반환한다.")
    void signUpWithInvalidEmail() throws Exception {
        //given
        final SignupRequest request = SignupRequest.builder()
                .email("test")
                .password("test1234!")
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일 형식이 올바르지 않습니다."));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("회원가입을 할 때 비밀번호가 null 혹은 비어있으면 400을 반환한다.")
    void signUpWithNullPassword(final String password) throws Exception {
        //given
        final SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password(password)
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 null이 될 수 없습니다."));
    }

    @Test
    @DisplayName("로그인을 한다.")
    void login() throws Exception {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";

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
    void loginWithNullEmail(final String email) throws Exception {
        //given
        final String password = "test1234!";

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일은 null이 될 수 없습니다."));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("로그인을 할 때 비밀번호가 null 혹은 비어있으면 400을 반환한다.")
    void loginWithNullPassword(final String password) throws Exception {
        //given
        final String email = "test@test.com";

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 null이 될 수 없습니다."));
    }

    @Test
    @DisplayName("리프레시 토큰을 이용해 로그아웃을 한다.")
    void logout() throws Exception {
        //when, then
        mockMvc.perform(
                        post("/api/auth/logout")
                                .header("Authorization", "Bearer refreshToken")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());

        BDDMockito.then(myLogoutHandler)
                .should(times(1))
                .logout(any(), any(), any());
    }

    @Test
    @DisplayName("리프레시 토큰을 이용해 토큰을 재발급 받는다.")
    void refresh() throws Exception {
        //given
        final RefreshRequest request = new RefreshRequest("refreshToken");

        given(authenticationService.refresh(request.refreshToken()))
                .willReturn(
                        AuthenticationResponse.builder()
                                .accessToken("newAccessToken")
                                .refreshToken("newRefreshToken")
                                .build()
                );

        //when, then
        mockMvc.perform(
                        post("/api/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("newRefreshToken"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("리프레시 토큰을 이용해 토큰을 재발급 받을 때 리프레시 토큰이 null 혹은 비어있으면 400을 반환한다.")
    void refreshWithNullRefreshToken(final String refreshToken) throws Exception {
        //given
        final RefreshRequest request = new RefreshRequest(refreshToken);

        //when, then
        mockMvc.perform(
                        post("/api/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("리프레시 토큰은 비어있을 수 없습니다."));
    }
}
