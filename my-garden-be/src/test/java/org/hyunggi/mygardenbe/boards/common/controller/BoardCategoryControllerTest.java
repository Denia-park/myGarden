package org.hyunggi.mygardenbe.boards.common.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.boards.common.response.BoardCategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardCategoryControllerTest extends ControllerTestSupport {
    @Test
    @DisplayName("공지사항 카테고리 목록을 조회한다.")
    void getCategories() throws Exception {
        //given
        final List<BoardCategoryResponse> categories = List.of(
                new BoardCategoryResponse("project", "프로젝트"),
                new BoardCategoryResponse("alarm", "알람"),
                new BoardCategoryResponse("study", "스터디"));

        BDDMockito.given(boardCategoryService.getCategories("notice"))
                .willReturn(categories);

        //when
        mockMvc.perform(
                        get("/api/boards/categories")
                                .queryParam("boardType", "notice")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code").value("project"))
                .andExpect(jsonPath("$.data[0].text").value("프로젝트"))
                .andExpect(jsonPath("$.data[1].code").value("alarm"))
                .andExpect(jsonPath("$.data[1].text").value("알람"))
                .andExpect(jsonPath("$.data[2].code").value("study"))
                .andExpect(jsonPath("$.data[2].text").value("스터디"));

        //then
        BDDMockito.verify(boardCategoryService).getCategories("notice");
    }

}
