package org.hyunggi.mygardenbe.boards.comment.controller;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.comment.service.CommentService;
import org.hyunggi.mygardenbe.boards.comment.service.response.CommentResponse;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 댓글 Controller
 */
@RestController
@RequestMapping("/api/boards/comment")
@RequiredArgsConstructor
public class CommentController {
    /**
     * 댓글 Service
     */
    private final CommentService commentService;

    /**
     * 게시판의 댓글을 조회한다.
     *
     * @param boardType 게시판 종류
     * @param boardId   게시판 ID
     * @return 댓글 목록
     */
    @GetMapping("/{boardType}/{boardId}")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable final String boardType, @PathVariable final Long boardId) {
        return ApiResponse.ok(commentService.getComments(boardType, boardId));
    }
}
