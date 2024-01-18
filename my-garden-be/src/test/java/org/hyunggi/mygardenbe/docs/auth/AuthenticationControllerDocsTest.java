package org.hyunggi.mygardenbe.docs.auth;

import org.hyunggi.mygardenbe.auth.controller.AuthenticationController;
import org.hyunggi.mygardenbe.auth.controller.request.RefreshRequest;
import org.hyunggi.mygardenbe.auth.controller.request.SignupRequest;
import org.hyunggi.mygardenbe.auth.service.AuthenticationService;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.hyunggi.mygardenbe.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerDocsTest extends RestDocsSupport {
    private final AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);

    @Override
    protected Object initController() {
        return new AuthenticationController(authenticationService);
    }

    @Test
    @DisplayName("회원 가입을 한다.")
    void signup() throws Exception {
        // given
        final SignupRequest request = new SignupRequest(
                "test@test.com",
                "test1234!"
        );

        BDDMockito.given(authenticationService.signUp(Mockito.any(), Mockito.any()))
                .willReturn(1L);

        // when, then
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1L))
                .andDo(document("auth/signup"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestFields(
                                fieldWithPath("email").description("회원 가입할 이메일").attributes(new Attributes.Attribute("constraints", "이메일 형식에 맞아야 합니다.")),
                                fieldWithPath("password").description("회원 가입할 비밀번호").attributes(new Attributes.Attribute("constraints", "비밀번호는 8자 이상 20자 이하여야 하며, 영문자, 숫자, 특수문자를 각각 1개 이상씩 포함해야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("회원 DB ID")
                        )
                ));
    }

    @Test
    @DisplayName("로그인을 한다.")
    void login() throws Exception {
        // given
        final SignupRequest request = new SignupRequest(
                "test@test.com",
                "test1234!"
        );

        BDDMockito.given(authenticationService.login(Mockito.any(), Mockito.any()))
                .willReturn(
                        AuthenticationResponse.builder()
                                .accessToken("accessToken")
                                .refreshToken("refreshToken")
                                .build()
                );

        // when, then
        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("refreshToken"))
                .andDo(document("auth/login"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestFields(
                                fieldWithPath("email").description("로그인할 이메일"),
                                fieldWithPath("password").description("로그인할 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰을 재발급 받는다.")
    void refresh() throws Exception {
        final RefreshRequest request = new RefreshRequest("refreshToken");

        // given
        BDDMockito.given(authenticationService.refresh(Mockito.any()))
                .willReturn(
                        AuthenticationResponse.builder()
                                .accessToken("newAccessToken")
                                .refreshToken("newRefreshToken")
                                .build()
                );

        // when, then
        mockMvc.perform(
                        post("/api/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("newRefreshToken"))
                .andDo(document("auth/refresh"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestFields(
                                fieldWithPath("refreshToken").description("기존 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("신규 액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("신규 리프레시 토큰")
                        )
                ));
    }
}
