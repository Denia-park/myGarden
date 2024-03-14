package org.hyunggi.mygardenbe.docs.board.learn;

import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.learn.controller.LearnBoardController;
import org.hyunggi.mygardenbe.boards.learn.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.learn.service.LearnBoardService;
import org.hyunggi.mygardenbe.boards.learn.service.response.LearnBoardResponse;
import org.hyunggi.mygardenbe.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.hyunggi.mygardenbe.docs.util.RestDocsUtil.field;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LearnBoardControllerDocsTest extends RestDocsSupport {
    private final LearnBoardService learnBoardService = Mockito.mock(LearnBoardService.class);

    @Override
    protected Object initController() {
        return new LearnBoardController(learnBoardService);
    }

    @Test
    @DisplayName("TIL 목록을 조회한다.")
    void getLearnBoards() throws Exception {
        //given
        BDDMockito.given(learnBoardService.getLearnBoards(any(), any(), any(), any(), any()))
                .willReturn(
                        CustomPage.of(
                                new PageImpl<>(buildLearnBoards(), PageRequest.of(0, 10), 1)
                        )
                );

        //when, then
        mockMvc.perform(
                        get("/api/boards/learn/list")
                                .param("startDate", "2024-02-01")
                                .param("endDate", "2024-02-01")
                                .param("category", "project")
                                .param("searchText", "검색어")
                                .param("currentPage", "1")
                                .param("pageSize", "10")
                                .param("sort", "writtenAt")
                                .param("order", "desc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("learn-board/get-learn-boards"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , queryParameters(
                                parameterWithName("startDate").description("조회 시작일")
                                        .attributes(field("constraints", "yyyy-MM-dd"))
                                        .optional(),
                                parameterWithName("endDate").description("조회 종료일")
                                        .attributes(field("constraints", "yyyy-MM-dd"))
                                        .optional(),
                                parameterWithName("category").description("분류")
                                        .attributes(field("constraints", "TIL 분류"))
                                        .optional(),
                                parameterWithName("searchText").description("검색어")
                                        .optional(),
                                parameterWithName("currentPage").description("현재 페이지")
                                        .attributes(field("constraints", "1부터 시작"))
                                        .optional(),
                                parameterWithName("pageSize").description("페이지 크기")
                                        .optional(),
                                parameterWithName("sort").description("정렬 기준")
                                        .attributes(field("constraints", "title, category, views, writtenAt"))
                                        .optional(),
                                parameterWithName("order").description("정렬 순서")
                                        .attributes(field("constraints", "asc, desc"))
                                        .optional()
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 (TIL 게시글)"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 게시글 수"),
                                fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("data.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.isFirst").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("data.isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("데이터 (TIL 게시글 목록 - 없으면 emptyList 반환)"),
                                fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("TIL 게시글 ID"),
                                fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("TIL 게시글 제목"),
                                fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("TIL 게시글 내용"),
                                fieldWithPath("data.content[].category").type(JsonFieldType.STRING).description("TIL 게시글 분류"),
                                fieldWithPath("data.content[].views").type(JsonFieldType.NUMBER).description("TIL 게시글 조회수"),
                                fieldWithPath("data.content[].writer").type(JsonFieldType.STRING).description("TIL 게시글 작성자"),
                                fieldWithPath("data.content[].writtenAt").type(JsonFieldType.STRING).description("TIL 게시글 작성일시")
                        )
                ));
    }

    private List<LearnBoardResponse> buildLearnBoards() {
        return List.of(
                LearnBoardResponse.builder()
                        .id(1L)
                        .title("TIL 제목")
                        .content("TIL 내용")
                        .category("TIL 분류")
                        .views(0)
                        .writer("작성자")
                        .writtenAt("2024-02-01 03:00:00")
                        .build()
        );
    }

    @Test
    @DisplayName("TIL을 조회한다.")
    void getLearnBoard() throws Exception {
        //given
        BDDMockito.given(learnBoardService.getLearnBoard(any()))
                .willReturn(
                        LearnBoardResponse.builder()
                                .id(1L)
                                .title("TIL 제목")
                                .content("TIL 내용")
                                .category("TIL 분류")
                                .views(0)
                                .writer("작성자")
                                .writtenAt("2024-02-01 03:00:00")
                                .build()
                );

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/boards/learn/{id}", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("learn-board/get-learn-board"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("id").description("조회할 TIL ID")
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 (TIL 게시글)"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("TIL 게시글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("TIL 게시글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("TIL 게시글 내용"),
                                fieldWithPath("data.category").type(JsonFieldType.STRING).description("TIL 게시글 분류"),
                                fieldWithPath("data.views").type(JsonFieldType.NUMBER).description("TIL 게시글 조회수"),
                                fieldWithPath("data.writer").type(JsonFieldType.STRING).description("TIL 게시글 작성자"),
                                fieldWithPath("data.writtenAt").type(JsonFieldType.STRING).description("TIL 게시글 작성일시")
                        )
                ));
    }

    @Test
    @DisplayName("TIL을 등록한다.")
    void postLearnBoard() throws Exception {
        //given
        String accessToken = "accessToken";

        final PostRequest request = PostRequest.builder()
                .title("TIL 제목")
                .category("TIL 분류")
                .content("TIL 내용")
                .build();

        BDDMockito.given(learnBoardService.postLearnBoard(any(), any(), any(), any()))
                .willReturn(1L);

        //when, then
        mockMvc.perform(
                        post("/api/boards/learn")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNumber())
                .andDo(document("learn-board/post-learn-board"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestHeaders(
                                headerWithName("Authorization").description("access token")
                                        .attributes(field("constraints", "Bearer {accessToken}"))
                        )
                        , requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("TIL 제목")
                                        .attributes(field("constraints", "100자 이내")),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("TIL 분류")
                                        .attributes(field("constraints", "20자 이내, TIL 분류")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("TIL 내용")
                                        .attributes(field("constraints", "4000자 이내"))
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터 (등록된 TIL ID)")
                        )
                ));
    }

    @Test
    @DisplayName("TIL을 수정한다.")
    void putLearnBoard() throws Exception {
        //given
        String accessToken = "accessToken";

        final PostRequest request = PostRequest.builder()
                .title("수정된 TIL 제목")
                .category("수정된 TIL 분류")
                .content("수정된 TIL 내용")
                .build();

        BDDMockito.given(learnBoardService.putLearnBoard(any(), any(), any(), any(), any()))
                .willReturn(1L);

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put("/api/boards/learn/{id}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNumber())
                .andDo(document("learn-board/put-learn-board"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("id").description("수정할 TIL ID")
                        )
                        , requestHeaders(
                                headerWithName("Authorization").description("access token")
                                        .attributes(field("constraints", "Bearer {accessToken}"))
                        )
                        , requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 TIL 제목")
                                        .attributes(field("constraints", "100자 이내")),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("수정할 TIL 분류")
                                        .attributes(field("constraints", "20자 이내, TIL 분류")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 TIL 내용")
                                        .attributes(field("constraints", "4000자 이내"))
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터 (수정된 TIL ID)")
                        )
                ));
    }

    @Test
    @DisplayName("TIL을 삭제한다.")
    void deleteLearnBoard() throws Exception {
        //given
        String accessToken = "accessToken";

        BDDMockito.given(learnBoardService.deleteLearnBoard(any(), any()))
                .willReturn(1L);

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/boards/learn/{id}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNumber())
                .andDo(document("learn-board/delete-learn-board"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("id").description("삭제할 TIL ID")
                        )
                        , requestHeaders(
                                headerWithName("Authorization").description("access token")
                                        .attributes(field("constraints", "Bearer {accessToken}"))
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터 (삭제된 TIL ID)")
                        )
                ));
    }
}
