package org.hyunggi.mygardenbe.boards.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.comment.controller.request.CommentRequest;
import org.hyunggi.mygardenbe.boards.comment.service.CommentService;
import org.hyunggi.mygardenbe.boards.comment.service.response.CommentResponse;
import org.hyunggi.mygardenbe.common.auth.annotation.WithLoginUserEntity;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글 Controller
 */
@RestController
@RequestMapping("/api/boards/comments")
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

    /**
     * 게시판의 댓글을 등록한다.
     */
    @PostMapping("/{boardType}/{boardId}")
    public ApiResponse<Long> postComment(@PathVariable final String boardType,
                                         @PathVariable final Long boardId,
                                         @RequestBody @Valid final CommentRequest request,
                                         @WithLoginUserEntity final MemberEntity member) {
        validatePostRequest(boardType, boardId, request);
        return ApiResponse.ok(commentService.postComment(boardType, boardId, request.comment(), member));
    }

    /**
     * 댓글 등록 요청을 검증한다.
     *
     * @param boardType 게시판 종류
     * @param boardId   게시판 ID
     * @param request   댓글 요청
     */
    private void validatePostRequest(final String boardType, final Long boardId, final CommentRequest request) {
        Assert.hasText(boardType, "게시판 종류는 비어있을 수 없습니다.");
        Assert.isTrue(boardId != null && boardId > 0, "게시판 ID는 0보다 커야 합니다.");
        Assert.notNull(request, "댓글 요청은 비어있을 수 없습니다.");
    }
}
