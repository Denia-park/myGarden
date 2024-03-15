package org.hyunggi.mygardenbe.boards.comment.controller;

import org.hyunggi.mygardenbe.ControllerTestSupportWithMockUser;
import org.hyunggi.mygardenbe.boards.comment.service.response.CommentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentEntityControllerTest extends ControllerTestSupportWithMockUser {
    @Test
    @DisplayName("게시판의 댓글을 조회한다.")
    void getComments() throws Exception {
        //given
        List<CommentResponse> comments = List.of(
                CommentResponse.builder()
                        .id(1L)
                        .content("내용 1")
                        .boardType("learn")
                        .boardId(1L)
                        .writer("tester")
                        .writtenAt(LocalDateTime.of(2024, 3, 15, 12, 0))
                        .memberId(1L)
                        .build()
        );

        BDDMockito.given(commentService.getComments("learn", 1L)).willReturn(comments);

        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/boards/comment/learn/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].content").value("내용 1"))
                .andExpect(jsonPath("$.data[0].boardType").value("learn"))
                .andExpect(jsonPath("$.data[0].boardId").value(1))
                .andExpect(jsonPath("$.data[0].writer").value("tester"))
                .andExpect(jsonPath("$.data[0].writtenAt").value("2024-03-15T12:00:00"))
                .andExpect(jsonPath("$.data[0].memberId").value(1));

        //then
        BDDMockito.verify(commentService).getComments("learn", 1L);
    }
}
