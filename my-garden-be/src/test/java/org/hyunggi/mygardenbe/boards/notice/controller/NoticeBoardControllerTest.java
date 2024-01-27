package org.hyunggi.mygardenbe.boards.notice.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NoticeBoardControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("공지사항을 조회한다.")
    void getDailyRoutine_withoutPagination() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("writtenAt").descending());

        List<NoticeBoardResponse> noticeBoardResponses = List.of(
                NoticeBoardResponse.builder()
                        .id(1L)
                        .title("공지사항 제목1")
                        .content("공지사항 내용1")
                        .category("공지")
                        .isImportant(true)
                        .views(0)
                        .writer("작성자1")
                        .writtenAt("2021-01-01 00:00:00")
                        .build()
        );

        BDDMockito.given(noticeBoardService.getNoticeBoards(any(), any(), any(), any(), any()))
                .willReturn(CustomPage.of(new PageImpl<>(noticeBoardResponses, pageable, noticeBoardResponses.size())));

        //when
        mockMvc.perform(
                        get("/api/boards/notice/list")
                                .queryParams(getQueryParams("2021-01-01", "2021-01-31", "공지", pageable))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L))
                .andExpect(jsonPath("$.data.content[0].title").value("공지사항 제목1"))
                .andExpect(jsonPath("$.data.content[0].content").value("공지사항 내용1"))
                .andExpect(jsonPath("$.data.content[0].category").value("공지"))
                .andExpect(jsonPath("$.data.content[0].isImportant").value(true))
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
        BDDMockito.verify(noticeBoardService).getNoticeBoards(
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 31),
                "공지",
                "",
                pageable
        );
    }

    private MultiValueMap<String, String> getQueryParams(final String startDate, final String endDate, final String category, final Pageable pageable) {
        final String currentPage = String.valueOf(pageable.getPageNumber());
        final String pageSize = String.valueOf(pageable.getPageSize());

        final String[] sortOrderString = pageable.getSort().toString().split(":");
        final String sort = sortOrderString[0];
        final String order = sortOrderString[1].toLowerCase().trim();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

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
}
