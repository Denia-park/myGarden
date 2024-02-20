package org.hyunggi.mygardenbe.docs.board.notice;

import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.notice.controller.NoticeBoardController;
import org.hyunggi.mygardenbe.boards.notice.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.notice.service.NoticeBoardService;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardResponse;
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

class NoticeBoardControllerDocsTest extends RestDocsSupport {
    private final NoticeBoardService noticeBoardService = Mockito.mock(NoticeBoardService.class);

    @Override
    protected Object initController() {
        return new NoticeBoardController(noticeBoardService);
    }

    @Test
    @DisplayName("공지사항 목록을 조회한다.")
    void getNoticeBoards() throws Exception {
        //given
        BDDMockito.given(noticeBoardService.getNoticeBoards(any(), any(), any(), any(), any()))
                .willReturn(
                        CustomPage.of(
                                new PageImpl<>(buildNoticeBoards(), PageRequest.of(0, 10), 1)
                        )
                );

        //when, then
        mockMvc.perform(
                        get("/api/boards/notice/list")
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
                .andDo(document("notice-board/get-notice-boards"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , queryParameters(
                                parameterWithName("startDate").description("조회 시작일")
                                        .attributes(field("constraints", "yyyy-MM-dd"))
                                        .optional(),
                                parameterWithName("endDate").description("조회 종료일")
                                        .attributes(field("constraints", "yyyy-MM-dd"))
                                        .optional(),
                                parameterWithName("category").description("카테고리")
                                        .attributes(field("constraints", "공지사항 카테고리"))
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
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 (공지사항 게시글)"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 게시글 수"),
                                fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("data.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.isFirst").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("data.isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("데이터 (공지사항 게시글 목록 - 없으면 emptyList 반환)"),
                                fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("공지사항 게시글 ID"),
                                fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("공지사항 게시글 제목"),
                                fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("공지사항 게시글 내용"),
                                fieldWithPath("data.content[].category").type(JsonFieldType.STRING).description("공지사항 게시글 카테고리"),
                                fieldWithPath("data.content[].isImportant").type(JsonFieldType.BOOLEAN).description("공지사항 게시글 중요 여부"),
                                fieldWithPath("data.content[].views").type(JsonFieldType.NUMBER).description("공지사항 게시글 조회수"),
                                fieldWithPath("data.content[].writer").type(JsonFieldType.STRING).description("공지사항 게시글 작성자"),
                                fieldWithPath("data.content[].writtenAt").type(JsonFieldType.STRING).description("공지사항 게시글 작성일시")
                        )
                ));
    }

    private List<NoticeBoardResponse> buildNoticeBoards() {
        return List.of(
                NoticeBoardResponse.builder()
                        .id(1L)
                        .title("공지사항 제목")
                        .content("공지사항 내용")
                        .category("공지사항 카테고리")
                        .isImportant(false)
                        .views(0)
                        .writer("작성자")
                        .writtenAt("2024-02-01 03:00:00")
                        .build()
        );
    }

    @Test
    @DisplayName("공지사항을 조회한다.")
    void getNoticeBoard() throws Exception {
        //given
        BDDMockito.given(noticeBoardService.getNoticeBoard(any()))
                .willReturn(
                        NoticeBoardResponse.builder()
                                .id(1L)
                                .title("공지사항 제목")
                                .content("공지사항 내용")
                                .category("공지사항 카테고리")
                                .isImportant(false)
                                .views(0)
                                .writer("작성자")
                                .writtenAt("2024-02-01 03:00:00")
                                .build()
                );

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/boards/notice/{id}", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("notice-board/get-notice-board"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("id").description("조회할 공지사항 ID")
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 (공지사항 게시글)"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("공지사항 게시글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("공지사항 게시글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("공지사항 게시글 내용"),
                                fieldWithPath("data.category").type(JsonFieldType.STRING).description("공지사항 게시글 카테고리"),
                                fieldWithPath("data.isImportant").type(JsonFieldType.BOOLEAN).description("공지사항 게시글 중요 여부"),
                                fieldWithPath("data.views").type(JsonFieldType.NUMBER).description("공지사항 게시글 조회수"),
                                fieldWithPath("data.writer").type(JsonFieldType.STRING).description("공지사항 게시글 작성자"),
                                fieldWithPath("data.writtenAt").type(JsonFieldType.STRING).description("공지사항 게시글 작성일시")
                        )
                ));
    }

    @Test
    @DisplayName("공지사항을 등록한다.")
    void postNoticeBoard() throws Exception {
        //given
        String accessToken = "accessTokenHasAdminRole";

        final PostRequest request = PostRequest.builder()
                .title("공지사항 제목")
                .category("공지사항 카테고리")
                .content("공지사항 내용")
                .isImportant(false)
                .build();

        BDDMockito.given(noticeBoardService.postNoticeBoard(any(), any(), any(), any(), any()))
                .willReturn(1L);

        //when, then
        mockMvc.perform(
                        post("/api/boards/notice")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNumber())
                .andDo(document("notice-board/post-notice-board"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestHeaders(
                                headerWithName("Authorization").description("access token (관리자 권한 필요)")
                                        .attributes(field("constraints", "Bearer {accessToken}"))
                        )
                        , requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목")
                                        .attributes(field("constraints", "100자 이내")),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("공지사항 카테고리")
                                        .attributes(field("constraints", "20자 이내, 공지사항 카테고리")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용")
                                        .attributes(field("constraints", "4000자 이내")),
                                fieldWithPath("isImportant").type(JsonFieldType.BOOLEAN).description("공지사항 중요 여부 (상단에 배치)")
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터 (등록된 공지사항 ID)")
                        )
                ));
    }

    @Test
    @DisplayName("공지사항을 수정한다.")
    void putNoticeBoard() throws Exception {
        //given
        String accessToken = "accessTokenHasAdminRole";

        final PostRequest request = PostRequest.builder()
                .title("수정된 공지사항 제목")
                .category("수정된 공지사항 카테고리")
                .content("수정된 공지사항 내용")
                .isImportant(false)
                .build();

        BDDMockito.given(noticeBoardService.putNoticeBoard(any(), any(), any(), any(), any(), any()))
                .willReturn(1L);

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put("/api/boards/notice/{id}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNumber())
                .andDo(document("notice-board/put-notice-board"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("id").description("수정할 공지사항 ID")
                        )
                        , requestHeaders(
                                headerWithName("Authorization").description("access token (관리자 권한 필요)")
                                        .attributes(field("constraints", "Bearer {accessToken}"))
                        )
                        , requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 공지사항 제목")
                                        .attributes(field("constraints", "100자 이내")),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("수정할 공지사항 카테고리")
                                        .attributes(field("constraints", "20자 이내, 공지사항 카테고리")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 공지사항 내용")
                                        .attributes(field("constraints", "4000자 이내")),
                                fieldWithPath("isImportant").type(JsonFieldType.BOOLEAN).description("수정할 공지사항 중요 여부 (상단에 배치)")
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터 (수정된 공지사항 ID)")
                        )
                ));
    }

    @Test
    @DisplayName("공지사항을 삭제한다.")
    void deleteNoticeBoard() throws Exception {
        //given
        String accessToken = "accessTokenHasAdminRole";

        BDDMockito.given(noticeBoardService.deleteNoticeBoard(any(), any()))
                .willReturn(1L);

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/boards/notice/{id}", 1L)
                                .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNumber())
                .andDo(document("notice-board/delete-notice-board"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("id").description("삭제할 공지사항 ID")
                        )
                        , requestHeaders(
                                headerWithName("Authorization").description("access token (관리자 권한 필요)")
                                        .attributes(field("constraints", "Bearer {accessToken}"))
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터 (삭제된 공지사항 ID)")
                        )
                ));
    }
}
