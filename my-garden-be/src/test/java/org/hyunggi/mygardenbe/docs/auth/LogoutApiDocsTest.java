package org.hyunggi.mygardenbe.docs.auth;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LogoutApiDocsTest extends ControllerTestSupport {
    @Test
    @DisplayName("로그아웃을 한다.")
    void logout() throws Exception {
        // given
        final String refreshToken = "refreshToken";

        // when, then
        mockMvc.perform(
                        post("/api/auth/logout")
                                .header("Authorization", "Bearer " + refreshToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("auth/logout"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestHeaders(headerWithName("Authorization").description("로그아웃할 리프레시 토큰 [Bearer {refreshToken}]"))
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 없음")
                        )
                ));
    }
}
