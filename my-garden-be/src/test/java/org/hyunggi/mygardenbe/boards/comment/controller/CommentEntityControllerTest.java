package org.hyunggi.mygardenbe.boards.comment.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.boards.comment.controller.request.CommentRequest;
import org.hyunggi.mygardenbe.boards.comment.service.response.CommentResponse;
import org.hyunggi.mygardenbe.mock.security.WithMyCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentEntityControllerTest extends ControllerTestSupport {
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
                        MockMvcRequestBuilders.get("/api/boards/comments/learn/1")
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

    @Test
    @DisplayName("게시판의 댓글을 등록한다.")
    @WithMyCustomUser
    void postComment() throws Exception {
        //given
        String boardType = "learn";
        Long boardId = 1L;
        String comment = "내용 1";
        CommentRequest request = new CommentRequest(comment);

        BDDMockito.given(commentService.postComment(any(), any(), any(), any())).willReturn(1L);

        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/boards/comments/%s/%d".formatted(boardType, boardId))
                                .content(objectMapper.writeValueAsString(request))
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));

        //then
        BDDMockito.verify(commentService).postComment(eq(boardType), eq(boardId), eq(comment), any());
    }

    @Test
    @DisplayName("게시판의 댓글을 삭제한다.")
    @WithMyCustomUser
    void deleteComment() throws Exception {
        //given
        String boardType = "learn";
        Long boardId = 1L;
        Long commentId = 1L;

        BDDMockito.given(commentService.deleteComment(any(), any(), any(), any())).willReturn(1L);

        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/boards/comments/%s/%d/%d".formatted(boardType, boardId, commentId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));

        //then
        BDDMockito.verify(commentService).deleteComment(eq(boardType), eq(boardId), eq(commentId), any());
    }
}
