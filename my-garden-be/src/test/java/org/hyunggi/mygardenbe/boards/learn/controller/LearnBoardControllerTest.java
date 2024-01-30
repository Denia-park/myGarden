package org.hyunggi.mygardenbe.boards.learn.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.learn.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.learn.service.response.LearnBoardResponse;
import org.hyunggi.mygardenbe.mock.security.WithMyCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LearnBoardControllerTest extends ControllerTestSupport {
    @Test
    @DisplayName("TIL 목록을 조회한다.")
    void getDailyRoutine_withoutPagination() throws Exception {
        //given
        final Pageable pageable = PageRequest.of(0, 10, Sort.by("writtenAt", "id").descending());

        final List<LearnBoardResponse> learnBoardResponses = List.of(
                LearnBoardResponse.builder()
                        .id(1L)
                        .title("TIL 제목1")
                        .content("TIL 내용1")
                        .category("CS")
                        .views(0)
                        .writer("작성자1")
                        .writtenAt("2021-01-01 00:00:00")
                        .build()
        );

        BDDMockito.given(learnBoardService.getLearnBoards(any(), any(), any(), any(), any()))
                .willReturn(CustomPage.of(new PageImpl<>(learnBoardResponses, pageable, learnBoardResponses.size())));

        //when
        mockMvc.perform(
                        get("/api/boards/learn/list")
                                .queryParams(getQueryParams("2021-01-01", "2021-01-31", "CS", pageable))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L))
                .andExpect(jsonPath("$.data.content[0].title").value("TIL 제목1"))
                .andExpect(jsonPath("$.data.content[0].content").value("TIL 내용1"))
                .andExpect(jsonPath("$.data.content[0].category").value("CS"))
                .andExpect(jsonPath("$.data.content[0].views").value(0))
                .andExpect(jsonPath("$.data.content[0].writer").value("작성자1"))
                .andExpect(jsonPath("$.data.content[0].writtenAt").value("2021-01-01 00:00:00"))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.currentPage").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.isFirst").value(true))
                .andExpect(jsonPath("$.data.isLast").value(true));

        //then
        BDDMockito.verify(learnBoardService).getLearnBoards(
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 31),
                "CS",
                "",
                pageable
        );
    }

    private MultiValueMap<String, String> getQueryParams(final String startDate, final String endDate, final String category, final Pageable pageable) {
        final String currentPage = String.valueOf(pageable.getPageNumber());
        final String pageSize = String.valueOf(pageable.getPageSize());

        final String[] sortOrderString = getFirstSort(pageable).split(": ");
        final String sort = sortOrderString[0];
        final String order = sortOrderString[1].toLowerCase();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        queryParams.add("startDate", startDate);
        queryParams.add("endDate", endDate);
        queryParams.add("category", category);
        queryParams.add("searchText", null);
        queryParams.add("currentPage", currentPage);
        queryParams.add("pageSize", pageSize);
        queryParams.add("sort", sort);
        queryParams.add("order", order);

        return queryParams;
    }

    private String getFirstSort(final Pageable pageable) {
        return pageable.getSort().toString().split(",")[0];
    }

    @Test
    @DisplayName("TIL을 조회한다.")
    void getLearnBoard() throws Exception {
        //given
        final LearnBoardResponse learnBoardResponse = LearnBoardResponse.builder()
                .id(1L)
                .title("TIL 제목1")
                .content("TIL 내용1")
                .category("CS")
                .views(0)
                .writer("작성자1")
                .writtenAt("2021-01-01 00:00:00")
                .build();

        BDDMockito.given(learnBoardService.getLearnBoard(any()))
                .willReturn(learnBoardResponse);

        //when
        mockMvc.perform(
                        get("/api/boards/learn/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("TIL 제목1"))
                .andExpect(jsonPath("$.data.content").value("TIL 내용1"))
                .andExpect(jsonPath("$.data.category").value("CS"))
                .andExpect(jsonPath("$.data.views").value(0))
                .andExpect(jsonPath("$.data.writer").value("작성자1"))
                .andExpect(jsonPath("$.data.writtenAt").value("2021-01-01 00:00:00"));

        //then
        BDDMockito.verify(learnBoardService).getLearnBoard(1L);
    }


    @Test
    @WithMyCustomUser
    @DisplayName("TIL을 작성한다.")
    void postLearnBoard() throws Exception {
        //given
        final PostRequest postRequest = PostRequest.builder()
                .title("TIL 제목1")
                .content("TIL 내용1")
                .category("CS")
                .build();

        BDDMockito.given(learnBoardService.postLearnBoard(any(), any()))
                .willReturn(1L);

        //when
        mockMvc.perform(
                        post("/api/boards/learn")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(postRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1L));

        //then
        BDDMockito.verify(learnBoardService).postLearnBoard(eq(postRequest), any());
    }

    @Test
    @WithMyCustomUser
    @DisplayName("TIL을 수정한다.")
    void putLearnBoard() throws Exception {
        //given
        final PostRequest postRequest = PostRequest.builder()
                .title("TIL 제목1")
                .content("TIL 내용1")
                .category("CS")
                .build();

        BDDMockito.given(learnBoardService.putLearnBoard(any(), any(), any()))
                .willReturn(1L);

        //when
        mockMvc.perform(
                        put("/api/boards/learn/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(postRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1L));

        //then
        BDDMockito.verify(learnBoardService).putLearnBoard(eq(1L), eq(postRequest), any());
    }

    @Test
    @WithMyCustomUser
    @DisplayName("TIL을 삭제한다.")
    void deleteLearnBoard() throws Exception {
        //given
        BDDMockito.given(learnBoardService.deleteLearnBoard(any(), any()))
                .willReturn(1L);

        //when
        mockMvc.perform(
                        delete("/api/boards/learn/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1L));

        //then
        BDDMockito.verify(learnBoardService).deleteLearnBoard(eq(1L), any());
    }
}
