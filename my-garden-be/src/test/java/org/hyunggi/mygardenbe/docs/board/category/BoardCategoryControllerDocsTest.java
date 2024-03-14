package org.hyunggi.mygardenbe.docs.board.category;

import org.hyunggi.mygardenbe.boards.common.category.controller.BoardCategoryController;
import org.hyunggi.mygardenbe.boards.common.category.response.BoardCategoryResponse;
import org.hyunggi.mygardenbe.boards.common.category.service.BoardCategoryService;
import org.hyunggi.mygardenbe.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.hyunggi.mygardenbe.docs.util.RestDocsUtil.field;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardCategoryControllerDocsTest extends RestDocsSupport {
    private final BoardCategoryService boardCategoryService = Mockito.mock(BoardCategoryService.class);

    @Override
    protected Object initController() {
        return new BoardCategoryController(boardCategoryService);
    }

    @Test
    @DisplayName("분류 목록을 조회한다.")
    void getCategories() throws Exception {
        //given
        BDDMockito.given(boardCategoryService.getCategories(any()))
                .willReturn(
                        List.of(
                                BoardCategoryResponse.builder()
                                        .code("study")
                                        .text("스터디")
                                        .build(),
                                BoardCategoryResponse.builder()
                                        .code("project")
                                        .text("프로젝트")
                                        .build()
                        )
                );

        //when, then
        mockMvc.perform(
                        get("/api/boards/categories")
                                .param("boardType", "notice")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("board-category/get-categories"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , queryParameters(
                                parameterWithName("boardType").description("게시판 타입")
                                        .attributes(field("constraints", "notice, learn"))
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("데이터 (분류 목록)"),
                                fieldWithPath("data[].code").type(JsonFieldType.STRING).description("분류 코드 (value)"),
                                fieldWithPath("data[].text").type(JsonFieldType.STRING).description("분류 텍스트 (label)")
                        )
                ));
    }
}
